package se.saidaspen.aoc2019.aoc17;

import se.saidaspen.aoc2019.aoc03.Point;
import se.saidaspen.aoc2019.aoc09.IntComputer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Aoc17 {

    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;

    private final Long[] code;

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        Aoc17 app = new Aoc17(input);
        app.part1();
    }

    Aoc17(String input) {
        code = Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
    }

    private static Map<Point, Character> map = new HashMap<>();

    // The first step is to calibrate the cameras by getting the alignment parameters of some well-defined points. Locate all scaffold intersections; for each, its alignment parameter is the distance between its left edge and the left edge of the view multiplied by the distance between its top edge and the top edge of the view. Here, the intersections from the above image are marked O:
    void part1() throws InterruptedException {
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10000);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
        IntComputer cpu = new IntComputer(code, in, out);
        pool.execute(cpu);
        pool.shutdown();
        pool.awaitTermination(100L, TimeUnit.DAYS);
        int currRow = 0;
        int currCol = 0;
        Long output = null;
        do {
            output = out.poll(3, TimeUnit.SECONDS);
            if (output == null) {
                break;
            }
            Character c = (char) output.byteValue();
            if (c == 10) {
                currRow++;
                currCol = 0;
            } else {
                map.put(new Point(currCol, currRow), c);
                currCol++;
            }
            System.out.print(c);
        } while(output != null);
        System.out.println("--------------------------");
        printMap(map);

        List<Point> crossings = new ArrayList<>();
        for (Point p : map.keySet()) {
            Point[] adjacent = getAdjacent(p);
            boolean crossing = true;
            if (!Character.valueOf('#').equals(map.get(p))) {
                continue;
            }
            for (Point adj : adjacent) {
                if (!Character.valueOf('#').equals(map.get(adj))) {
                    crossing = false;
                    break;
                }
            }
            if (crossing) {
                crossings.add(p);
            }
        }
        System.out.println("Crossings + " + Arrays.toString(crossings.toArray()));
        int alignment = 0;
        for(Point p : crossings) {
            alignment += p.x * p.y;
        }
        printMap(map);
        System.out.println("Alignment: " + alignment);
        List<Path> paths = findAllPaths(map);
        List<List<Byte>> commands = paths.stream().map(this::toCommands).collect(Collectors.toList());
        List<List<Byte>> recurring = findRecurring(commands);
        List<List<Byte>> validPaths = recurring.stream().filter(l -> l.size() <= 4).map(this::compress).collect(Collectors.toList());
    }

    private List<Byte> compress(List<Byte> l) {
        // Make movements compressed
        return new ArrayList<>();
    }

    private List<List<Byte>> findRecurring(List<List<Byte>> commands) {
        return new ArrayList<>();
    }

    private List<Byte> toCommands(Path p) {
        return new ArrayList<>();
    }

    private List<Path> findAllPaths(Map<Point, Character> map) {
        // Make copy of map
        // Walk to the next cross road
        // on crossroad do recursive calls to finAllPaths for each possible road
        // if all tiles in map has been visited, return
        return new ArrayList<>();
    }

    private static class Path {

    }

    private int toDir(Character dir) {
        if (dir.equals('^')) {
            return NORTH;
        } else if (dir.equals('v')) {
            return SOUTH;
        } else if (dir.equals('<')) {
            return WEST;
        } else if (dir.equals('>')){
            return EAST;
        } else {
            throw new RuntimeException("Unsupported dir " + dir);
        }
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

    public static Point[] getAdjacent(Point p) {
        Point[] result = new Point[4];
        result[SOUTH] = new Point(p.x, p.y + 1);
        result[NORTH] = new Point(p.x, p.y - 1);
        result[WEST] = new Point(p.x - 1, p.y);
        result[EAST] = new Point(p.x + 1, p.y);
        return result;
    }

    private static void printMap(Map<Point, Character> map) {
        System.out.print("\033[2J"); // Clear screen
        var largestX = 0;
        var largestY = 0;
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
        for (int i = 0 ; i < lines.size(); i++) {
            List<Character> row = lines.get(i);
            StringBuilder sb = new StringBuilder();
            for (Character c : row) {
                sb.append(c);
            }
            System.out.println(sb);
        }
    }
}
