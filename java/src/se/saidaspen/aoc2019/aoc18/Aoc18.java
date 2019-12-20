package se.saidaspen.aoc2019.aoc18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.logging.Logger;

import se.saidaspen.aoc2019.Point;

public class Aoc18 {

    private final Map<Point, Character> map;
    private final char[] KEY_SYMBOLS = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private final char[] DOOR_SYMBOLS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final Map<Character, Point> keys;
    private final Map<Point, Character> keysByPos = new HashMap<>();
    private final Map<Point, Character> doorsByPos = new HashMap<>();
    private final HashMap<Character, Point> poi;
    private int allKeys = 0;

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        Aoc18 app = new Aoc18(input);
    }

    Aoc18(String input) {
        map = new HashMap<>();
        String[] lines = input.split("\n");
        for (int row = 0; row < lines.length; row++) {
            char[] chars = lines[row].toCharArray();
            for (int col = 0; col < chars.length; col++) {
                map.put(Point.of(col, row), chars[col]);
            }
        }
        poi = new HashMap<>();
        Map<Character, Point> doors = findAllDoors(map);
        for (Map.Entry<Character, Point> entry : doors.entrySet()) {
            doorsByPos.put(entry.getValue(), entry.getKey());
        }
        keys = findAllKeys(map);
        for (Character k : keys.keySet()) {
            allKeys = allKeys | toInt(k);
        }
        for (Map.Entry<Character, Point> entry : keys.entrySet()) {
            keysByPos.put(entry.getValue(), entry.getKey());
        }
        poi.putAll(doors);
        poi.putAll(keys);
        poi.put('@', find(map, '@').get(0));
    }

    public int part1() {
        HashMap<Point, Character> cleanMap = new HashMap<>(map);
        // Clean up the map (remove all POIs)
        for (Point p : poi.values()) {
            cleanMap.put(p, '.');
        }
        // Get all walkable tiles
        List<Point> openPoints = find(cleanMap, '.');
        Map<Point, Node> nodes = new HashMap<>();
        // Create nodes
        for (Point p : openPoints) {
            nodes.put(p, new Node(p));
        }
        // Fill the nodes, setting the neighbouring nodes
        for (Point p : nodes.keySet()) {
            Node node = nodes.get(p);
            Point[] adjacent = p.getAdjacent();
            for (Point adj : adjacent) {
                if (adj != null && Character.valueOf('.').equals(cleanMap.get(adj))) {
                    node.addNeighbour(nodes.get(adj));
                }
            }
        }
        // Populate distances between all POIs
        Map<Long, Requirement> distances = populateDistances(nodes);

        //Find best path
        Trip shortest = getShortestPathToAllKeys(distances);
        System.out.println("Shortest sequence: " + shortest.keys);
        return shortest.distance;
    }

    private Trip getShortestPathToAllKeys(Map<Long, Requirement> distances) {
        Queue<Trip> queue = new ArrayDeque<>();
        queue.add(new Trip('@', 0, 0));
        Trip shortest = new Trip('@', 0, Integer.MAX_VALUE);
        Map<Long, Integer> tested = new HashMap<>();
        while (!queue.isEmpty()) {
            Trip current = queue.remove();
            if (isComplete(current.keys)) {
                if (current.distance < shortest.distance) {
                    shortest.distance = current.distance;
                }
                continue;
            }
            int currentKeys = current.keys;
            if (!current.first.equals('@')) {
                currentKeys = currentKeys | toInt(current.first);
            }
            Long unique = identifierOf(current.first, currentKeys);
            Integer testedVal = tested.get(unique);
            if (testedVal != null) {
                if (testedVal < current.distance)
                    continue;
            } else {
                tested.put(unique, current.distance);
            }
            for (Map.Entry<Long, Requirement> to : distances.entrySet()) {
                if  ((to.getKey() & 0xFFFF0000L >> 32) != toInt(current.first){

                }
                Requirement requirement = distances.get(current.first).get(to.getKey());
                if (requirement == null) {
                    continue;
                }
                int distForNext = current.distance + requirement.dist;
                if (distForNext > shortest.distance
                        || to.getKey().equals('@')
                        || current.first.equals(to.getKey())
                        || contains(current.keys, to.getKey())
                        || contains(requirement.keysOnTheWay, to.getKey())) {
                    continue;
                }

                boolean haveKeysRequired = hasAllKeys(currentKeys, to.getValue().requiredKeys);
                //Boolean haveKeysRequired = to.getValue().requiredKeys.parallelStream().map(k -> currentKeys.contains(k.toString())).reduce(true, (a, b) -> a && b);
                if (haveKeysRequired) {
                    queue.add(new Trip(to.getKey(), add(currentKeys, to.getKey()), distForNext));
                }
            }
        }
        return shortest;
    }

    private int add(int currentKeys, Character key) {
        return currentKeys | toInt(key);
    }

    private int toInt(Character c) {
        return 1 << c - 'a';
    }

    private boolean contains(int keys, Character key) {
        return (keys & toInt(key)) > 0;
    }

    private Long identifierOf(Character first, int keys) {
        long val = 1L << ((first - 'a') + 32);
        val = val | keys;
        return val;
    }

    private boolean hasAllKeys(int listOfKeys, int keys) {
        return (listOfKeys & keys) == keys;
    }

    private boolean isComplete(int listOfKeys) {
        return hasAllKeys(listOfKeys, allKeys);
    }

    private Map<Long, Requirement> populateDistances(Map<Point, Node> nodes) {
        Map<Long, Requirement> distances = new HashMap<>();
        List<Character> pois = new ArrayList<>(this.keys.keySet());
        pois.add('@');
        for (Character a : pois) {
            for (Character b : pois) {
                if (a.equals(b)) {
                    continue;
                }
                long placeA = ((long) toInt(a)) << 32;
                long placeB = toInt(a);
                if (!distances.containsKey(placeA|placeB)) {
                    Requirement distance = getDistance(nodes, a, b);
                    distances.put(placeA|placeB, distance);
                    distances.put(placeB|placeA, distance);
                }
            }
        }
        return distances;
    }

    //BFS to find distances between to POIs
    private Requirement getDistance(Map<Point, Node> nodes, Character from, Character to) {
        Point currPoint = poi.get(from);
        Node currNode = nodes.get(currPoint);
        Point toPoint = poi.get(to);
        List<Point> visited = new ArrayList<>();
        Map<Node, Requirement> requirements = new HashMap<>();
        requirements.put(currNode, new Requirement(0, 0, 0));
        Queue<Node> visitQueue = new ArrayDeque<>();
        visitQueue.add(currNode);
        while (!visitQueue.isEmpty()) {
            Node thisNode = visitQueue.remove();
            if (thisNode.data.equals(toPoint)) {
                return requirements.get(thisNode);
            }
            Character door = doorsByPos.get(thisNode.data);
            if (door != null) {
                Character key = door.toString().toLowerCase().charAt(0);
                Requirement req = requirements.get(thisNode);
                req.requiredKeys = add(req.requiredKeys, key);
            }
            Character key = keysByPos.get(thisNode.data);
            if (key != null && !key.equals(to) && !key.equals(from)) {
                Requirement req = requirements.get(thisNode);
                req.keysOnTheWay = add(req.keysOnTheWay, key);
            }
            visited.add(thisNode.data);
            for (Node n : thisNode.neighbours) {
                if (!visited.contains(n.data)) {
                    Requirement parentReq = requirements.get(thisNode);
                    requirements.put(n, new Requirement(parentReq.dist + 1, parentReq.requiredKeys, parentReq.keysOnTheWay));
                    visitQueue.add(n);
                }
            }
        }
        return new Requirement(Integer.MAX_VALUE, 0, 0);
    }

    private static class Node {
        private Point data;
        private List<Node> neighbours;

        Node(Point data) {
            this.data = data;
            this.neighbours = new LinkedList<>();
        }

        void addNeighbour(Node n) {
            this.neighbours.add(n);
        }

        @Override
        public String toString() {
            return "Node{" + data + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return data.equals(node.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }
    }

    private Map<Character, Point> findAllKeys(Map<Point, Character> map) {
        Map<Character, Point> doorsMap = new HashMap<>();
        List<Point> doorPositions = find(map, KEY_SYMBOLS);
        doorPositions.forEach(p -> doorsMap.put(map.get(p), p));
        return doorsMap;
    }

    private Map<Character, Point> findAllDoors(Map<Point, Character> map) {
        Map<Character, Point> doorsMap = new HashMap<>();
        List<Point> doorPositions = find(map, DOOR_SYMBOLS);
        doorPositions.forEach(p -> doorsMap.put(map.get(p), p));
        return doorsMap;
    }

    private static List<Point> find(Map<Point, Character> map, char... chars) {
        List<Point> points = new ArrayList<>();
        for (Point p : map.keySet()) {
            for (char x : chars) {
                if (map.get(p).equals(x)) {
                    points.add(p);
                }
            }
        }
        return points;
    }

    private static class Requirement {
        private Integer dist;
        private int requiredKeys;
        private int keysOnTheWay;

        public Requirement(Integer dist, int requiredKeys, int keysOnTheWay) {
            this.dist = dist;
            this.requiredKeys = requiredKeys;
            this.keysOnTheWay = keysOnTheWay;
        }

        @Override
        public String toString() {
            return String.format("Requirement{dist=%s, required keys:%s", dist, requiredKeys);
        }
    }

    private class Trip {
        private final Character first;
        private int keys;
        private int distance;

        public Trip(Character first, int keys, int distance) {
            this.first = first;
            this.keys = keys;
            this.distance = distance;
        }
    }
}
