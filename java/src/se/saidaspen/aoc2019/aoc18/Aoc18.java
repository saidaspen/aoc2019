package se.saidaspen.aoc2019.aoc18;

import lombok.Value;
import se.saidaspen.aoc2019.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Character.toLowerCase;

public class Aoc18 {

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        System.out.println(new Aoc18().part1(input));
    }

    int part1(String input) {
        NeptuneMap map = parseMap(input);
        return solve(map);
    }

    private static class NeptuneMap {
        Set<Point> wall = new HashSet<>();
        Map<Point, Integer> keys = new HashMap<>();
        Map<Point, Integer> doors = new HashMap<>();

        Point startPos;
    }

    @Value
    private static class BfsNode {
        PositionState posState;
        int distance;
    }

    @Value
    private static class PositionState {
        Point pos;
        int keys;
    }

    private int solve(NeptuneMap map) {
        Queue<BfsNode> queue = new ArrayDeque<>();
        queue.add(new BfsNode(new PositionState(map.startPos, 0), 0));
        int allKeys = map.keys.values().stream().reduce(0, (a, b) -> a | b);
        int shortest = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            BfsNode current = queue.remove();
            if (current.posState.keys == allKeys) {
                shortest = Math.min(current.distance, shortest);
            }
            Set<BfsNode> neighbours = getNeighbours(map, current.posState, current.distance);
            queue.addAll(neighbours);
        }
        return shortest;
    }

    private final Map<PositionState, Integer> shortMemo = new HashMap<>();

    private Set<BfsNode> getNeighbours(NeptuneMap map, PositionState robot, int currDistance) {
        Integer previousLen = shortMemo.get(robot);
        if (previousLen != null && previousLen <= currDistance) {
            return new HashSet<>();
        } else {
            shortMemo.put(robot, currDistance);
        }
        Set<BfsNode> neighbours = new HashSet<>();
        for (Map.Entry<Point, Integer> key : map.keys.entrySet()) {
            Integer keyId = key.getValue();
            int heldKeys = robot.keys;
            if ((keyId & heldKeys) == keyId) { // Early exit if already have key
                continue;
            }
            long requirement = getRequirement(map, robot.pos, key.getKey());
            if (requirement != 0) {
                int keyReq = (int) (requirement >> 32);
                int distance = (int) ((requirement & 0x0000FFFF));
                if ((robot.keys & keyReq) == keyReq) {
                    neighbours.add(new BfsNode(new PositionState(key.getKey(), key.getValue() | robot.keys), distance + currDistance));
                }
            }
        }
        //neighbourMemo.put(robot, neighbours);
        return neighbours;
    }

    private Map<Long, Long> distanceCache = new HashMap<>();

    private long getRequirement(NeptuneMap map, Point from, Point to) {
        // Starting position mapped internally as int 1
        int fromKey = from.equals(map.startPos) ? 1 : map.keys.get(from);
        int toKey = map.keys.get(to);
        long cacheKey = (((long) fromKey) << 32);
        cacheKey |= toKey;
        Long cachedDist = distanceCache.get(cacheKey);
        if (cachedDist != null) {
            return cachedDist;
        } else {
            long req = bfsFindDist(map, from, to);
            distanceCache.put(cacheKey, req);
            long invertedCacheKey = invert(cacheKey);
            distanceCache.put(invertedCacheKey, req);
            return req;
        }
    }

    private long invert(long cacheKey) {
        long invertedCacheKey = (cacheKey & 0xFFFF0000) >> 32;
        invertedCacheKey |= (cacheKey & 0x0000FFFF) << 32;
        return invertedCacheKey;
    }

    Map<Point, Map<Point, Long>> distCache = new HashMap<>();

    private long bfsFindDist(NeptuneMap map, Point from, Point to) {
        Map<Point, Long> fromCache = distCache.computeIfAbsent(from, k -> new HashMap<>());
        fromCache.computeIfAbsent(to, t -> {
            Set<Point> visited = new HashSet<>();
            // The BFS Node is modeled as int[4] = {x, y, distance, requiredKeys}
            // this is only used locally in this method, not to be shared outside of it.
            Queue<int[]> visitQueue = new ArrayDeque<>();
            visitQueue.add(new int[]{from.x, from.y, 0, 0});
            visited.add(from);
            while (!visitQueue.isEmpty()) {
                int[] current = visitQueue.remove();
                int dist = current[2];
                int reqKeys = current[3];
                Point[] adjacent = Point.of(current[0], current[1]).getAdjacent();
                for (Point adj : adjacent) {
                    if (adj == null)
                        continue;
                    if (adj.x == to.x && adj.y == to.y) {
                        return (((long) reqKeys) << 32) | (dist + 1);
                    }
                    if (visited.add(adj)) {
                        Integer door = map.doors.get(adj);
                        if (!map.wall.contains(adj)) {
                            if (door == null) {
                                door = 0;
                            }
                            visitQueue.add(new int[]{adj.x, adj.y, dist + 1, reqKeys | door});
                        }
                    }
                }
            }
            return 0L;
        });
        return fromCache.get(to);
    }

    private NeptuneMap parseMap(String input) {
        NeptuneMap result = new NeptuneMap();
        String[] lines = input.split("\n");
        for (int row = 0; row < lines.length; row++) {
            char[] chars = lines[row].toCharArray();
            for (int col = 0; col < chars.length; col++) {
                char c = chars[col];
                Point p = Point.of(col, row);
                if (c == '#') {
                    result.wall.add(p);
                } else if (c >= 'a' && c <= 'z') {
                    result.keys.put(p, toInt(c));
                } else if (c >= 'A' && c <= 'Z') {
                    result.doors.put(p, toInt(toLowerCase(c)));
                } else if (c == '@') {
                    result.startPos = p;
                }
            }
        }
        return result;
    }

    private int keyNum = 1;
    private Map<Character, Integer> keyNumbers = new HashMap<>();

    private int toInt(Character c) {
        if (!keyNumbers.containsKey(c)) {
            keyNumbers.put(c, (1 << keyNum));
            keyNum++;
        }
        return keyNumbers.get(c);
    }
    
}
