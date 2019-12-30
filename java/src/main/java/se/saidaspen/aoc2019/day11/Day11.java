package se.saidaspen.aoc2019.day11;

import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.IntComputer;
import se.saidaspen.aoc2019.Point;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Solution to Advent of Code 2019 Day 11
 * The original puzzle can be found here: https://adventofcode.com/2019/day/11
 */
public final class Day11 implements Day {

    private final Long[] code;

    public Day11(String input) {
        code = Arrays.stream(input.split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
    }

    @Override
    public String part1() {
        return Integer.toString(run(null).keySet().size());
    }

    @Override
    public String part2() {
        return printMap(run(1L));
    }

    public Map<Point, Long> run(Long starVal) {
        try {
            ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10000);
            ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
            if (starVal != null) {
                in.put(starVal);
            }
            IntComputer cpu = new IntComputer(code, in, out);
            Robot robot = new Robot(0, 0, out, in);
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
            pool.execute(cpu);
            pool.execute(robot);
            pool.shutdown();
            pool.awaitTermination(10L, TimeUnit.SECONDS);
            return robot.panelColors;
        } catch (Exception e) {
            throw new RuntimeException("Exception while painting hull.", e);
        }
    }

    private String printMap(Map<Point, Long> panelColors) {
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
        return render(lines);
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
            outMap.put(Point.of(p.x + Math.abs(minX), p.y + Math.abs(minY)), panelColors.get(p));
        }
        return outMap;
    }

    private String render(List<List<Long>> lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = lines.size() - 1; i >= 0; i--) {
            List<Long> row = lines.get(i);
            for (Long aLong : row) {
                sb.append(aLong == 0 ? "░" : "█");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
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
        private final Map<Point, Long> panelColors = new HashMap<>();

        public Robot(int x, int y, ArrayBlockingQueue<Long> input, ArrayBlockingQueue<Long> output) {
            this.x = x;
            this.y = y;
            this.dir = 0; // Start facing up.
            this.input = input;
            this.output = output;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    var colorOver = panelColors.getOrDefault(Point.of(x, y), 0L);
                    output.put(colorOver);
                    var color = input.poll(TIMEOUT, TimeUnit.SECONDS);
                    var turnDir = input.poll(TIMEOUT, TimeUnit.SECONDS);
                    if (color == null || turnDir == null) { // Timed out. Finished.
                        return;
                    }
                    panelColors.put(Point.of(x, y), color == 0L ? 0L : 1L);
                    dir = turnDir == 0 ? (dir + 270) % 360 : (dir + 90) % 360;
                    y = dir == 0 ? y + 1 : dir == 180 ? y - 1 : y;
                    x = dir == 90 ? x + 1 : dir == 270 ? x - 1 : x;
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }
}
