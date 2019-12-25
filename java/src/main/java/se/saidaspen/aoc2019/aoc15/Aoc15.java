package se.saidaspen.aoc2019.aoc15;

import se.saidaspen.aoc2019.IntComputer;
import se.saidaspen.aoc2019.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Aoc15 {

    public final static int NORTH = 0;
    public final static int SOUTH = 1;
    public final static int WEST = 2;
    public final static int EAST = 3;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private static final boolean PRINT = true;
    static final Character TILE_WALL = '█';
    static final Character TILE_OXYGEN_GEN = 'X';
    static final Character TILE_OXYGEN = 'O';

    private static final long MODE_MAP = 1L;
    private static final long MODE_OXYGEN = 2L;
    private static final char TILE_ROBOT = 'R';
    private static final char TILE_KNOWN = '.';
    public static final char TILE_EMPTY = ' ';

    private final Long[] code;
    private static Map<Point, Character> map = new HashMap<>();
    private static Long[] display = new Long[]{0L, 0L, 0L, 0L};

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        Aoc15 aoc15 = new Aoc15(input);
        RepairBot robot = new RepairBot(aoc15.code, Point.of(0, 0), new WallSearch());
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        pool.execute(robot);
        pool.shutdown();
        pool.awaitTermination(100L, TimeUnit.DAYS);
        printMap(map);
        aoc15.simulateOxygenSpread(map);
    }

    private Aoc15(String input) {
        code = Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
    }

    private void simulateOxygenSpread(Map<Point, Character> map) throws InterruptedException {
        display[3] = MODE_OXYGEN;
        Point oxygenStartPos = find(map, TILE_OXYGEN_GEN).get(0);
        map.put(oxygenStartPos, TILE_OXYGEN);
        List<Point> oxygenPoints;
        Long cnt = 1L;
        do {
            Thread.sleep(20L);
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
            printMap(map);
            display[2] = cnt;
        } while (!isFilled());
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

    private static void printMap(Map<Point, Character> panelColors) {
        if (!PRINT) {
            return;
        }
        System.out.print("\033[2J"); // Clear screen
        Map<Point, Character> canvas = reorient(panelColors);
        var largestX = 0;
        var largestY = 0;
        for (Point p : canvas.keySet()) {
            largestX = Math.max(p.x, largestX);
            largestY = Math.max(p.y, largestY);
        }
        List<List<Character>> lines = emptyLines(largestX, largestY);
        for (Point p : canvas.keySet()) {
            lines.get(p.y).set(p.x, canvas.get(p));
        }
        render(lines);
        String mode = "";
        if (display[3] == 0) {
            mode = "SEARCH OXYGEN";
        } else if (display[3] == 1) {
            mode = "FILL MAP";
        } else if (display[3] == 2) {
            mode = "FILL OXYGEN";
        }
        System.out.println("Mode: " + mode + "\t" + "Robot steps: " + display[0] + "\t" + "Closest path: " + display[1] + "\t" + "Oxygen spread (s): " + display[2]);
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

    private static Map<Point, Character> reorient(Map<Point, Character> map) {
        Map<Point, Character> outMap = new HashMap<>();
        int minX = 0;
        int minY = 0;
        for (Point p : map.keySet()) {
            minX = Math.min(p.x, minX);
            minY = Math.min(p.y, minY);
        }
        for (Point p : map.keySet()) {
            outMap.put(Point.of(p.x + Math.abs(minX), p.y + Math.abs(minY)), map.get(p));
        }
        return outMap;
    }

    private static void render(List<List<Character>> lines) {
        for (int i = lines.size() - 1; i >= 0; i--) {
            List<Character> row = lines.get(i);
            StringBuilder sb = new StringBuilder();
            for (Character c : row) {
                sb.append(colorize(c));
            }
            System.out.println(sb);
        }
    }

    private static String colorize(Character c) {
        if (c == TILE_OXYGEN) {
            return ANSI_BLUE_BACKGROUND + " " + ANSI_RESET;
        } else if (c == TILE_OXYGEN_GEN) {
            return ANSI_RED + "X" + ANSI_RESET;
        } else if (c == TILE_KNOWN) {
            return ANSI_GREEN_BACKGROUND + " " + ANSI_RESET;
        } else {
            return c.toString();
        }
    }

    private static class RepairBot implements Runnable {
        private final SearchStrategy strategy;
        private final Stack<Point> log = new Stack<>();
        private final IntComputer cpu;
        private ArrayBlockingQueue<Long> input, output;
        private Point pos;
        private int dir = 1;
        long step = 0;
        private Point oxygenPos;

        RepairBot(Long[] code, Point start, SearchStrategy strategy) {
            this.input = new ArrayBlockingQueue<>(10000);
            this.output = new ArrayBlockingQueue<>(10000);
            cpu = new IntComputer(code, output, input);
            pos = start;
            this.strategy = strategy;
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
                    display[0] = step;
                    if (step % 100 == 0) {
                        printMap(map);
                    }
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
                        display[1] = (long) log.size();
                        display[3] = MODE_MAP;
                        addToMap(TILE_OXYGEN_GEN);
                        move();
                        oxygenPos = pos;
                    }
                    dir = strategy.getNext(pos, dir, map);
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
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
