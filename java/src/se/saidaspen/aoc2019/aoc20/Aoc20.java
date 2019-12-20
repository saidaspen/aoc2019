package se.saidaspen.aoc2019.aoc20;

import se.saidaspen.aoc2019.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public final class Aoc20 {

    private final int width;
    private final int height;
    private final Map<Point, Character> map = new HashMap<>();
    private final Map<Point, String> portals = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        Aoc20 app = new Aoc20(input);
        System.out.println(app.run());
    }

    Aoc20(String input) {
        List<String> lines = Arrays.stream(input.split("\n")).collect(Collectors.toList());
        width = lines.get(0).length();
        height = lines.size();
        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < width; col++) {
                char c = lines.get(row).charAt(col);
                if (isChar(c)) { // Portal
                    Point portalLoc;
                    String portalName;
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
                    portals.put(portalLoc, portalName);
                } else if (c != ' ') {
                    map.put(Point.of(col, row), c);
                }
            }
        }
    }

    private static class Position {
        private final Point point;
        private final int level;

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

        Position(Point from, int i) {
            point = from;
            level = i;
        }
    }

    private static class BfsNode {
        Position pos;
        int dist;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BfsNode position = (BfsNode) o;
            return dist == position.dist &&
                    Objects.equals(pos, position.pos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, dist);
        }

        BfsNode(Position pos, int i) {
            this.pos = pos;
            dist = i;
        }
    }

    private boolean isChar(char c) {
        return c >= 'A' && c <= 'Z';
    }

    int run() {
        // Fill the nodes, setting the neighbouring nodes
        return getDistance(findPortal("AA").get(0).getKey(), findPortal("ZZ").get(0).getKey());
    }

    private int getDistance(Point from, Point to) {
        Queue<BfsNode> visitQueue = new ArrayDeque<>();
        visitQueue.add(new BfsNode(new Position(from, 0), 0));
        Position target = new Position(to, 0);
        Set<Position> visited = new HashSet<>();
        while (visitQueue.size() > 0) {
            BfsNode current = visitQueue.remove();
            if (current.pos.equals(target)) {
                return current.dist;
            }
            visited.add(current.pos);
            List<Position> neighbours = getNeighbours(current.pos);
            for (Position n : neighbours) {
                if (visited.contains(n)) {
                    continue;
                }
                visitQueue.add(new BfsNode(n, current.dist + 1));
            }
        }
        return -1;
    }

    private static final Map<Position, List<Position>> nbrMemo = new HashMap<>();

    private List<Position> getNeighbours(Position pos) {
        if (nbrMemo.containsKey(pos)) {
            return nbrMemo.get(pos);
        }
        List<Position> neighbours = new ArrayList<>();
        // Normal neighbours
        Point[] adjacent = pos.point.getAdjacent();
        for (Point adj : adjacent) {
            if (adj != null && Character.valueOf('.').equals(map.get(adj))) {
                neighbours.add(new Position(adj, pos.level));
            }
        }
        String portal = portals.get(pos.point);
        if (portal != null && !"AA".equals(portal)) {
            int lvl;
            if (pos.point.x <= 2 || pos.point.x >= width - 3 || pos.point.y <= 2 || pos.point.y == height - 3) {
                if (pos.level != 0)
                    lvl = pos.level - 1;
                else {
                    nbrMemo.put(pos, neighbours);
                    return neighbours;
                }
            } else {
                lvl = pos.level + 1;
            }
            List<Map.Entry<Point, String>> portalPoints = findPortal(portal);
            for (Map.Entry<Point, String> p : portalPoints) {
                if (!p.getKey().equals(pos.point)) {
                    neighbours.add(new Position(p.getKey(), lvl));
                }
            }
        }
        nbrMemo.put(pos, neighbours);
        return neighbours;
    }

    private List<Map.Entry<Point, String>> findPortal(String portalName) {
        List<Map.Entry<Point, String>> result = new ArrayList<>();
        for (Map.Entry<Point, String> entry : portals.entrySet()) {
            if (entry.getValue().equals(portalName)) {
                result.add(entry);
            }
        }
        return result;
    }
}
