package se.saidaspen.aoc2019.aoc11;

import se.saidaspen.aoc2019.IntComputer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Aoc11 {

    private static Map<Point, Long> panelColors = new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        new Aoc11().run(input);
    }

    private static final class Point {
        private int x, y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point p = (Point) o;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public void run(String input) throws InterruptedException {
        Long[] code = Arrays.stream(input.split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10000);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
        Long starVal = 1L; // Give it a starting value of 1, for white. Part 2.
        in.put(starVal);
        IntComputer cpu = new IntComputer(code, in, out);
        Robot robot = new Robot(0, 0, out, in);
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        pool.execute(cpu);
        pool.execute(robot);
        pool.shutdown();
        pool.awaitTermination(100L, TimeUnit.DAYS);
        System.out.println(String.format("Painted %s panels at least once", panelColors.keySet().size()));
        printMap(panelColors);
    }

    private void printMap(Map<Point, Long> panelColors) {
        Map<Point, Long> canvas = reorient(panelColors);
        var largestX = 0;
        var largestY = 0;
        for (Point p : canvas.keySet()) {
            largestX = Math.max(p.x, largestX);
            largestY = Math.max(p.y, largestY);
        }
        List<List<Long>> lines = emptyLines(largestX, largestY);
        for (Point p : canvas.keySet()) {
            lines.get(p.y).set(p.x, canvas.get(p));
        }
        render(lines);
    }

    private Map<Point, Long> reorient(Map<Point, Long> panelColors) {
        Map<Point, Long> outMap = new HashMap<>();
        int minX = 0;
        int minY = 0;
        for (Point p : panelColors.keySet()) {
            minX = Math.min(p.x, minX);
            minY = Math.min(p.y, minY);
        }
        for (Point p : panelColors.keySet()) {
            outMap.put(new Point(p.x + Math.abs(minX), p.y + Math.abs(minY)), panelColors.get(p));
        }
        return outMap;
    }

    private void render(List<List<Long>> lines) {
        for (int i = lines.size() - 1; i >= 0; i--) {
            List<Long> row = lines.get(i);
            StringBuilder sb = new StringBuilder();
            for (Long aLong : row) {
                sb.append(aLong == 0 ? "░" : "█");
            }
            System.out.println(sb);
        }
    }

    private List<List<Long>> emptyLines(int xMax, int yMax) {
        List<List<Long>> lines = new ArrayList<>();
        for (int row = 0; row <= yMax; row++) {
            List<Long> r = new ArrayList<>();
            for (int col = 0; col <= xMax; col++) {
                r.add(0L);
            }
            lines.add(r);
        }
        return lines;
    }

    private static class Robot implements Runnable {
        public static final int TIMEOUT = 1;
        private int x, y, dir;
        private ArrayBlockingQueue<Long> input, output;

        public Robot(int x, int y, ArrayBlockingQueue<Long> input, ArrayBlockingQueue<Long> output) {
            this.x = x;
            this.y = y;
            this.dir = 0; // Start facing up.
            this.input = input;
            this.output = output;
        }

        @Override
        public void run() {
            System.out.println("Robot starting...");
            try {
                while (true) {
                    var colorOver = panelColors.getOrDefault(new Point(x, y), 0L);
                    output.put(colorOver);
                    var color = input.poll(TIMEOUT, TimeUnit.SECONDS);
                    var turnDir = input.poll(TIMEOUT, TimeUnit.SECONDS);
                    if (color == null || turnDir == null) { // Timed out. Finished.
                        return;
                    }
                    System.out.print(String.format("Painted (%s, %s): %s. ", x, y, (color == 0L ? "black" : "white")));
                    panelColors.put(new Point(x, y), color == 0L ? 0L : 1L);
                    dir = turnDir == 0 ? (dir + 270) % 360 : (dir + 90) % 360;
                    y = dir == 0 ? y + 1 : dir == 180 ? y - 1 : y;
                    x = dir == 90 ? x + 1 : dir == 270 ? x - 1 : x;
                    System.out.print(String.format("Moved to (%s, %s)\n", x, y));
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }
}
