package se.saidaspen.aoc2019.aoc20;

import se.saidaspen.aoc2019.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Aoc20Org {

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        Aoc20Org app = new Aoc20Org(input);
        System.out.println(app.part1());
    }

    Map<Point, Character> map = new HashMap<>();
    Map<Point, Portal> portals = new HashMap<>();

    Aoc20Org(String input) {
        parse(input);
    }

    private int getDistance(Map<Point, Node> nodes, Point from, Point to) {
        //BFS to find distances between to POIs
        Point currPoint = from;
        Node currNode = nodes.get(currPoint);
        List<Point> visited = new ArrayList<>();
        Map<Node, Integer> lengths = new HashMap<>();
        lengths.put(currNode, 0);
        int shortest = Integer.MAX_VALUE;
        Queue<Node> visitQueue = new ArrayDeque<>();
        visitQueue.add(currNode);
        while (!visitQueue.isEmpty()) {
            Node thisNode = visitQueue.remove();
            if (thisNode.data.equals(to)) {
                if (lengths.get(thisNode) < shortest)
                    shortest = lengths.get(thisNode);
            }
            visited.add(thisNode.data);
            for (Node n : thisNode.neighbours) {
                if (!visited.contains(n.data)) {
                    lengths.put(n, lengths.get(thisNode) + 1);
                    visitQueue.add(n);
                }
            }
        }
        return shortest;
    }


    private static class Position {
        Point point;
        int level;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return level == position.level &&
                    Objects.equals(point, position.point);
        }

        @Override
        public int hashCode() {
            return Objects.hash(point, level);
        }

        public Position(Point from, int i) {
            point = from;
            level = i;
        }
    }

    private static class Item {
        Position pos;
        int distance;

        public Item(Position startPos, int i) {
            pos = startPos;
            distance = i;
        }
    }

    private int getDistance2(Map<Point, Node> nodes, Point from, Point to) {
        Item startItem = new Item(new Position(from, 0), 0);
        List<Position> visited = new ArrayList<>();
        Queue<Pair<Item, String>> visitQueue = new ArrayDeque<>();
        visitQueue.add(new Pair<>(startItem, ""));
        visited.add(startItem.pos);
        while (!visitQueue.isEmpty()) {
            Pair<Item, String> thisItem = visitQueue.remove();
            if (thisItem.first.pos.point.equals(to) && thisItem.first.pos.level == 0) {
                System.out.println(thisItem.second);
                return thisItem.first.distance;
            }
            Node thisNode = nodes.get(thisItem.first.pos.point);
            for (Node n : thisNode.neighbours) {
                Portal thisTilePortal = portals.get(n.data);
                Portal neighbourPortal = portals.get(thisNode.data);
                Portal portal = thisTilePortal != null
                        && neighbourPortal != null
                        && (thisItem.first.pos.level != 0 || neighbourPortal.levelOffset > 1)
                        ? neighbourPortal : null;
                int levelOffset = 0;
                if (portal!= null && !portal.id.equals("AA")) {
                    levelOffset = portal.levelOffset;
                }
                Position nextPosition = new Position(n.data, thisItem.first.pos.level + levelOffset);
                String log = portal == null ? thisItem.second : thisItem.second + "->" + portal.id + "(" + thisItem.first.pos.level + ")";
                if (nextPosition.level >= 0 && !visited.contains(nextPosition)) {
                    visited.add(nextPosition);
                    visitQueue.add(new Pair(new Item(nextPosition, thisItem.first.distance + 1), log));
                }
            }
        }
        return -1;
    }

    private List<Map.Entry<Point, Portal>> findPortal(String portalName) {
        List<Map.Entry<Point, Portal>> result = new ArrayList<>();
        for (Map.Entry<Point, Portal> entry : portals.entrySet()) {
            if (entry.getValue().id.equals(portalName)) {
                result.add(entry);
            }
        }
        return result;
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

    private static class Portal {
        final String id;
        final Point loc;
        final int levelOffset;

        public Portal(String id, Point loc, int levelOffset) {
            this.id = id;
            this.loc = loc;
            this.levelOffset = levelOffset;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Portal portal = (Portal) o;
            return levelOffset == portal.levelOffset &&
                    Objects.equals(id, portal.id) &&
                    Objects.equals(loc, portal.loc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, loc, levelOffset);
        }
    }

    private void parse(String input) {
        List<String> lines = Arrays.stream(input.split("\n")).collect(Collectors.toList());
        int width = lines.get(0).length();
        int height = lines.size();
        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < width; col++) {
                Character c = lines.get(row).charAt(col);
                if (c == ' ') {
                    continue;
                } else if (c >= 'A' && c <= 'Z') { // Portal
                    Point portalLoc = null;
                    String portalName = null;
                    if (col < width - 1 && isChar(lines.get(row).charAt(col + 1))) {
                        portalName = "" + c + lines.get(row).charAt(col + 1);
                        if (col == 0 || (col >= width / 2 && col != width - 2)) {
                            portalLoc = Point.of(col + 2, row);
                        } else {
                            portalLoc = Point.of(col - 1, row);
                        }
                    } else if (row < height - 1 && isChar(lines.get(row + 1).charAt(col))) {
                        portalName = "" + c + lines.get(row + 1).charAt(col);
                        if (row == 0 || (row >= height / 2 && row != height - 2)) {
                            portalLoc = Point.of(col, row + 2);
                        } else {
                            portalLoc = Point.of(col, row - 1);
                        }
                    } else {
                        continue;
                    }
                    assert portalLoc != null;
                    assert !"".equals(portalName) && portalName.length() == 2;
                    int offset;
                    if (portalLoc.x <= 2 || portalLoc.x >= width - 3) {
                        offset = -1;
                    } else if (portalLoc.y <= 2 || portalLoc.y == height -3) {
                        offset = -1;
                    } else {
                        offset = 1;
                    }
                    Portal portal = new Portal(portalName, portalLoc, offset);
                    portals.put(portalLoc, portal);
                } else {
                    map.put(Point.of(col, row), c);
                }
            }
        }
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

    private boolean isChar(char c) {
        return (c >= 'A' && c <= 'Z');
    }

    // The first step is to calibrate the cameras by getting the alignment parameters of some well-defined points. Locate all scaffold intersections; for each, its alignment parameter is the distance between its left edge and the left edge of the view multiplied by the distance between its top edge and the top edge of the view. Here, the intersections from the above image are marked O:
    int part1() throws InterruptedException {
        List<Point> openPoints = find(map, '.');
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
                if (adj != null && Character.valueOf('.').equals(map.get(adj))) {
                    node.addNeighbour(nodes.get(adj));
                }
            }
            if (portals.containsKey(p)) {
                String portalName = portals.get(p).id;
                if (!portalName.equals("AA")&& !portalName.equals("ZZ")) {
                    for (Map.Entry<Point, Portal> entry : portals.entrySet()) {
                        if (entry.getValue().id.equals(portalName) && !entry.getKey().equals(p)) {
                            Node n = nodes.get(entry.getKey());
                            assert n != null;
                            node.addNeighbour(n);
                        }
                    }
                }
            }
        }

        // Get shortest path
        int distance = getDistance2(nodes, findPortal("AA").get(0).getKey(), findPortal("ZZ").get(0).getKey());
        return distance;
    }

    private class Pair<T, S> {
        T first;
        S second;
        public Pair(T first, S second) {
            this.first = first;
            this.second = second;
        }
    }
}
