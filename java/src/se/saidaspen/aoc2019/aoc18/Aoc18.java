// Create a hashmap for all POIs
// Clone the map and remove all POIs
// Calculate the distance from between all POIs (locks, keys and start point) using BFS


package se.saidaspen.aoc2019.aoc18;

import se.saidaspen.aoc2019.aoc03.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

public class Aoc18 {


    public static final int TOP_LEFT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int BOTTOM_LEFT = 3;
    public static final int BOTTOM_RIGHT = 4;

    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    public static final int CENTER_LINES = 0;

    private final Map<Point, Character> map;
    private final char[] KEY_SYMBOLS = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private final char[] DOOR_SYMBOLS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static int largestX;
    private static int largestY;
    private final Random rnd = new Random();
    private final boolean filterQuadrant;
    private int maxSteps;

    private Map<String, Integer> distances = new HashMap<>();
    private final Map<Character, Point> doors;
    private final Map<Character, Point> keys;
    private final HashMap<Character, Point> poi;


    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        Aoc18 app = new Aoc18(input, false, 50_000);
    }

    Aoc18(String input, boolean filterQuadrant, int maxSteps) {
        this.filterQuadrant = filterQuadrant;
        this.maxSteps = maxSteps;
        map = new HashMap<>();
        String[] lines = input.split("\n");
        for (int row = 0; row < lines.length; row++) {
            char[] chars = lines[row].toCharArray();
            for (int col = 0; col < chars.length; col++) {
                map.put(new Point(col, row), chars[col]);
            }
        }
        poi = new HashMap<Character, Point>();
        doors = findAllDoors(map);
        keys = findAllKeys(map);
        poi.putAll(doors);
        poi.putAll(keys);
        poi.put('@', find(map, '@').get(0));
        printMap(map);
    }

    public int part1() {
        HashMap<Point, Character> cleanMap = new HashMap<>(map);
        for (Point p : poi.values()) {
            cleanMap.put(p, '.');
        }
        List<Point> openPoints = find(cleanMap, '.');
        Map<Point, TreeNode> nodes = new HashMap<>();
        for (Point p : openPoints) {
            nodes.put(p, new TreeNode<>(p));
        }
        for (Point p : nodes.keySet()) {
            TreeNode node = nodes.get(p);
            Point[] adjacent = getAdjacent(p);
            for (Point adj : adjacent) {
                if (adj != null && Character.valueOf('.').equals(cleanMap.get(adj))) {
                    node.addNeighbour(nodes.get(adj));
                }
            }
        }
        return 0;
    }

    private TreeNode toTree(HashMap<Point, Character> cleanMap) {
        return null;
    }

    private static class TreeNode<Point> {
        private Point data;
        private List<TreeNode<Point>> neighbours;

        public TreeNode(Point data) {
            this.data = data;
            this.neighbours = new LinkedList<>();
        }

        public void addNeighbour(TreeNode<Point> n) {
            this.neighbours.add(n);
        }
    }

    public int part1Old() {
        long before = System.nanoTime();
        Map<Point, Character> mapClone = new HashMap<>(map);
        List<MapObj> keys = findAllKeysOld(mapClone);
        Map<Character, Point> doors = findAllDoors(mapClone);
        List<String> results = new ArrayList<>();
        findAllCombinations(doors, "", mapClone, results, keys.size());
        int shortest = Integer.MAX_VALUE;
        for (String path : results) {
            shortest = Math.min(shortest, lengthOf(path));
        }
        System.out.println("Took: " + ((System.nanoTime() - before) / 1_000_000_000) + "s");
        return shortest;
    }

    private int lengthOf(String path) {
        Map<Point, Character> mapClone = new HashMap<>(map);
        for (Point p : doors.values()) {
            mapClone.put(p, '.');
        }
        int length = 0;
        for (char c : path.toCharArray()) {
            Point pos = find(mapClone, '@').get(0);
            Point target = keys.get(c);
            length += stepsTo(mapClone, target, false);
            mapClone.put(pos, '.');
            mapClone.put(target, '@');
        }
        return length;
    }

    private void findAllCombinations(Map<Character, Point> doors, String startList, Map<Point, Character> m, List<String> results, int stopCrit) {
        List<MapObj> keysToLookFor = findAllKeysOld(m);
        ArrayList<MapObj> possibleKeys = possibleKeys(m, keysToLookFor);
        for (MapObj k : possibleKeys) {
            Point doorPos = doors.get(k.symbol.toString().toUpperCase().charAt(0));
            m.put(k.pos, '.');
            if (doorPos != null) {
                m.put(doorPos, '.');
            }
            findAllCombinations(doors, startList + k.symbol, m, results, stopCrit);
            m.put(k.pos, k.symbol);
            if (doorPos != null) {
                m.put(doorPos, k.symbol.toString().toUpperCase().charAt(0));
            }
        }
        int length = startList.length();
        if (length == stopCrit)
            results.add(startList);
    }

    private ArrayList<MapObj> possibleKeys(Map<Point, Character> m, List<MapObj> keys) {
        ArrayList<MapObj> possibleKeys = new ArrayList<>();
        for (MapObj k : keys) {
            int stepsToKey = stepsTo(m, k.pos, filterQuadrant);
            if (stepsToKey > 0) {
                possibleKeys.add(k);
            }
        }
        return possibleKeys;
    }

    private int stepsTo(Map<Point, Character> map, Point target, boolean filterQuad) {
        Map<Point, Character> mapClone = new HashMap<>(map);
        Point startPos = find(mapClone, '@').get(0);
        for (Point otherKeyPoint : keys.values()) {
            if (!otherKeyPoint.equals(target) && !otherKeyPoint.equals(startPos) && !Character.valueOf('.').equals(mapClone.get(otherKeyPoint))) {
                mapClone.put(otherKeyPoint, '#');
            }
        }
        if (filterQuad) {
            int keyQuadrant = quadant(target);
            mapClone.keySet().stream().filter(p -> quadant(p) != keyQuadrant && quadant(p) != CENTER_LINES).forEach(p -> mapClone.put(p, '#'));
        }
        mapClone.put(startPos, '.');
        int step = 0;
        Stack<Point> visited = new Stack<>();
        Point pos = startPos;
        while (step < maxSteps) {
            if (visited.contains(pos)) {
                //noinspection StatementWithEmptyBody
                while (!visited.pop().equals(pos)) {
                }
            }
            visited.push(pos);
            Point[] adjacentPoints = getAdjacent(pos);
            List<Point> candidates = new ArrayList<>(4);
            for (Point p : adjacentPoints) {
                if (p != null) {
                    if (p.equals(target)) {
                        return visited.size();
                    }
                    if (!p.equals(visited.peek()) && Character.valueOf('.').equals(mapClone.get(p))) {
                        candidates.add(p);
                    }
                }
            }
            pos = candidates.get(rnd.nextInt(candidates.size()));
            step++;
        }
        return -1;
    }

    private static Point[] getAdjacent(Point p) {
        Point[] result = new Point[4];
        result[SOUTH] = new Point(p.x, p.y + 1);
        result[NORTH] = new Point(p.x, p.y - 1);
        result[WEST] = new Point(p.x - 1, p.y);
        result[EAST] = new Point(p.x + 1, p.y);
        return result;
    }

    private int quadant(Point p) {
        if (p == null) {
            new Exception().printStackTrace();
        }
        if (p.x < largestX / 2 && p.y < largestY / 2) {
            return TOP_LEFT;
        } else if (p.x > largestX / 2 && p.y < largestY / 2) {
            return TOP_RIGHT;
        } else if (p.x < largestX / 2 && p.y > largestY / 2) {
            return BOTTOM_LEFT;
        } else if (p.x > largestX / 2 && p.y > largestY / 2) {
            return BOTTOM_RIGHT;
        } else {
            return CENTER_LINES;
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

    private List<MapObj> findAllKeysOld(Map<Point, Character> map) {
        List<Point> keyPositions = find(map, KEY_SYMBOLS);
        return keyPositions.stream().map(p -> new MapObj(p, map.get(p))).collect(Collectors.toList());
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

    private static void printMap(Map<Point, Character> map) {
        System.out.print("\033[2J"); // Clear screen
        largestX = 0;
        largestY = 0;
        for (Point p : map.keySet()) {
            largestX = Math.max(p.x, largestX);
            largestY = Math.max(p.y, largestY);
        }
        List<List<Character>> lines = emptyLines(largestX, largestY);
        for (Point p : map.keySet()) {
            lines.get(p.y).set(p.x, map.get(p));
        }
        render(lines);
    }

    private static List<List<Character>> emptyLines(int xMax, int yMax) {
        List<List<Character>> lines = new ArrayList<>();
        for (int row = 0; row <= yMax; row++) {
            List<Character> r = new ArrayList<>();
            for (int col = 0; col <= xMax; col++) {
                r.add(' ');
            }
            lines.add(r);
        }
        return lines;
    }

    private static void render(List<List<Character>> lines) {
        for (List<Character> row : lines) {
            StringBuilder sb = new StringBuilder();
            for (Character c : row) {
                sb.append(c);
            }
            System.out.println(sb);
        }
    }

    private static class MapObj {
        Point pos;
        Character symbol;

        public MapObj(Point p, Character c) {
            pos = p;
            symbol = c;
        }

        public String toString() {
            return String.format("{%s:%s}", symbol, pos.toString());
        }
    }

}
