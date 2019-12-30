package se.saidaspen.aoc2019.day15;

import se.saidaspen.aoc2019.AocUtil;
import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.IntComputer;
import se.saidaspen.aoc2019.Point;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Solution to Advent of Code 2019 Day 15
 * The original puzzle can be found here: https://adventofcode.com/2019/day/15
 */
public class Day15 implements Day {

    public final static int NORTH = 0;
    public final static int SOUTH = 1;
    public final static int WEST = 2;
    public final static int EAST = 3;

    static final Character TILE_WALL = '█';
    static final Character TILE_OXYGEN_GEN = 'X';
    static final Character TILE_OXYGEN = 'O';

    private static final char TILE_ROBOT = 'R';
    private static final char TILE_KNOWN = '.';

    private final Long[] code;
    private static Map<Point, Character> map = new HashMap<>();

    Day15(String input) {
        code = AocUtil.toLongCode(input);
    }

    private long simulateOxygenSpread(Map<Point, Character> map) {
        Point oxygenStartPos = find(map, TILE_OXYGEN_GEN).get(0);
        map.put(oxygenStartPos, TILE_OXYGEN);
        List<Point> oxygenPoints;
        long cnt = 0L;
        do {
            oxygenPoints = find(map, TILE_OXYGEN);
            for (Point p : oxygenPoints) {
                Point[] adjacent = getAdjacent(p);
                for (Point adj : adjacent) {
                    if (map.containsKey(adj) && !map.get(adj).equals(TILE_WALL)) {
                        map.put(adj, TILE_OXYGEN);
                    }
                }
            }
            cnt++;
        } while (!isFilled());
        return cnt;
    }

    private boolean isFilled() {
        for (Character c : map.values()) {
            if (c.equals(TILE_KNOWN)) {
                return false;
            }
        }
        return true;
    }

    public static Point[] getAdjacent(Point p) {
        Point[] result = new Point[4];
        result[NORTH] = Point.of(p.x, p.y + 1);
        result[SOUTH] = Point.of(p.x, p.y - 1);
        result[WEST] = Point.of(p.x - 1, p.y);
        result[EAST] = Point.of(p.x + 1, p.y);
        return result;
    }

    private static List<Point> find(Map<Point, Character> map, char x) {
        List<Point> points = new ArrayList<>();
        for (Point p : map.keySet()) {
            if (map.get(p).equals(x)) {
                points.add(p);
            }
        }
        return points;
    }

    @Override
    public String part1() {
        try {
            RepairBot robot = new RepairBot(code, Point.of(0, 0));
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
            pool.execute(robot);
            pool.shutdown();
            pool.awaitTermination(20L, TimeUnit.SECONDS);
            return Long.toString(robot.closestPath);
        } catch (Exception e) {
            throw new RuntimeException("Exception while running simulation.", e);
        }
    }

    @Override
    public String part2() {
        try {
            RepairBot robot = new RepairBot(code, Point.of(0, 0));
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
            pool.execute(robot);
            pool.shutdown();
            pool.awaitTermination(100L, TimeUnit.DAYS);
            return Long.toString(simulateOxygenSpread(map));
        } catch (Exception e) {
            throw new RuntimeException("Exception while running simulation.", e);
        }
    }

    private static class RepairBot implements Runnable {
        private final Stack<Point> log = new Stack<>();
        private final IntComputer cpu;
        private ArrayBlockingQueue<Long> input, output;
        private Point pos;
        private int dir = 1;
        long step = 0;
        private Point oxygenPos;
        private long closestPath = 0;

        RepairBot(Long[] code, Point start) {
            this.input = new ArrayBlockingQueue<>(10000);
            this.output = new ArrayBlockingQueue<>(10000);
            cpu = new IntComputer(code, output, input);
            pos = start;
            map.put(pos, TILE_ROBOT);
        }

        @Override
        public void run() {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
            pool.execute(cpu);
            pool.shutdown();
            try {
                while (!mapKnown()) {
                    step++;
                    logPosition();
                    output.put((long) dir);
                    Long status = input.poll(5, TimeUnit.SECONDS);
                    if (status == null) {
                        return;
                    } else if (status == 0) {
                        addToMap(TILE_WALL);
                    } else if (status == 1) {
                        move();
                    } else if (status == 2) {
                        if (closestPath == 0) {
                            closestPath = log.size();
                        }
                        addToMap(TILE_OXYGEN_GEN);
                        move();
                        oxygenPos = pos;
                    }
                    dir = getNext(pos, dir, map);
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        public int getNext(Point pos, int dir, Map<Point, Character> map) {
            Point[] adjacent = getAdjacent(pos);
            int rightOf;
            int leftOf;
            int back;
            if (dir == 1) {
                back = 2;
                leftOf = 3;
                rightOf = 4;
            } else if (dir == 2) {
                back = 1;
                leftOf = 4;
                rightOf = 3;
            } else if (dir == 3) {
                back = 4;
                leftOf = 2;
                rightOf = 1;
            } else if (dir == 4) {
                back = 3;
                leftOf = 1;
                rightOf = 2;
            } else {
                throw new RuntimeException("Unsupported direction " + dir);
            }
            if (!isWall(map, adjacent[(4 + rightOf - 1) % 4])) {
                return rightOf;
            } else if (!isWall(map, adjacent[(4 + dir - 1) % 4])) {
                return dir;
            } else if (!isWall(map, adjacent[(4 + leftOf - 1) % 4])) {
                return leftOf;
            } else if ((!isWall(map, adjacent[(4 + back - 1) % 4]))) {
                return back;
            } else {
                throw new RuntimeException("Surrounded by walls!");
            }
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        private boolean isWall(Map<Point, Character> map, Point rightOf) {
            return map.containsKey(rightOf) && TILE_WALL.equals(map.get(rightOf));
        }

        private void logPosition() {
            if (log.contains(pos)) {
                //noinspection StatementWithEmptyBody
                while (!log.pop().equals(pos)) {
                }
            }
            log.push(pos);
        }

        private boolean mapKnown() {
            boolean hasExplored = false;
            for (Point p : map.keySet()) {
                if (Character.valueOf(TILE_KNOWN).equals(map.get(p)) || Character.valueOf(TILE_ROBOT).equals(map.get(p))) {
                    hasExplored = true;
                    Point[] adjacent = getAdjacent(p);
                    for (Point adj : adjacent) {
                        Character adjChar = map.get(adj);
                        if (adjChar == null || Character.valueOf(' ').equals(adjChar)) {
                            return false;
                        }
                    }
                }
            }
            return hasExplored;
        }

        private void addToMap(char tile) {
            if (dir == 1) {
                map.put(Point.of(pos.x, pos.y + 1), tile);
            } else if (dir == 2) {
                map.put(Point.of(pos.x, pos.y - 1), tile);
            } else if (dir == 3) {
                map.put(Point.of(pos.x - 1, pos.y), tile);
            } else if (dir == 4) {
                map.put(Point.of(pos.x + 1, pos.y), tile);
            }
        }

        private void move() {
            map.remove(pos);
            if (pos.x == 0 && pos.y == 0) {
                map.put(pos, 'S');
            } else if (oxygenPos != null && pos.x == oxygenPos.x && pos.y == oxygenPos.y) {
                map.put(pos, 'X');
            } else {
                map.put(pos, TILE_KNOWN);
            }
            if (dir == 1) {
                pos = Point.of(pos.x, pos.y + 1);
            } else if (dir == 2) {
                pos = Point.of(pos.x, pos.y - 1);
            } else if (dir == 3) {
                pos = Point.of(pos.x - 1, pos.y);
            } else if (dir == 4) {
                pos = Point.of(pos.x + 1, pos.y);
            }
            map.put(pos, TILE_ROBOT);
        }
    }
}
