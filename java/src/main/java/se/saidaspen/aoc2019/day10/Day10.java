package se.saidaspen.aoc2019.day10;

import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Solution to Advent of Code 2019 Day 10
 * The original puzzle can be found here: https://adventofcode.com/2019/day/10
 */
public class Day10 implements Day {

    private int width;
    private int height;
    private String input;

    public Day10(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public Day10(String input) {
        this.input = input;
    }

    public static Point findShot(int i, List<List<Integer>> laserShootMap) {
        for (int row = 0; row < laserShootMap.size(); row++) {
            for (int col = 0; col < laserShootMap.get(0).size(); col++) {
                if (laserShootMap.get(row).get(col) == i) {
                    return Point.of(col, row);
                }
            }
        }
        throw new RuntimeException("Unable to find shot " + i);
    }

    public static List<List<Integer>> laserShootMap(String input) {
        List<List<String>> map = mapOf(input);
        Point laserPos = getLaserPos(map);
        if (laserPos == null) {
            List<List<Integer>> detectMap = detectMap(input);
            laserPos = findBest(detectMap);
        }
        int height = map.size();
        int width = map.get(0).size();
        return new Day10(height, width).laser(laserPos, map);
    }

    public static List<List<Integer>> detectMap(String input) {
        List<List<String>> map = mapOf(input);
        int height = map.size();
        int width = map.get(0).size();
        return new Day10(height, width).detect(map);
    }

    private static List<List<String>> mapOf(String input) {
        List<String> lines = Arrays.stream(input.split("\n")).collect(Collectors.toList());
        return lines.stream()
                .map(l -> l.chars().mapToObj(e -> (char) e).map(Object::toString).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public List<List<Integer>> laser(Point laserPos, List<List<String>> map) {
        List<Point> astroids = getAstroids(map);
        List<List<Integer>> laserMap = emptyMap();
        int i = 1;
        while (!astroids.isEmpty()) {
            astroids.remove(laserPos);
            List<Point> detectableAsteroids = getDetectable(laserPos, astroids);
            detectableAsteroids = detectableAsteroids.stream()
                    .sorted(Comparator.comparingDouble(a -> angleOf(laserPos, a)))
                    .collect(Collectors.toList());
            for (Point a : detectableAsteroids) {
                astroids.remove(a);
                laserMap.get(a.y).set(a.x, i++);
            }
        }
        return laserMap;
    }

    public static double angleOf(Point center, Point to) {
        double radians = Math.atan2((to.y - center.y) * 1.0, (to.x - center.x) * 1.0);
        radians = (radians + 2 * Math.PI) % (2 * Math.PI); // Make positive
        return (radians + Math.PI / 2) % (2 * Math.PI); // Translate to our Pointinates
    }

    public List<List<Integer>> detect(List<List<String>> map) {
        List<Point> astroids = getAstroids(map);
        List<List<Integer>> losMap = emptyMap();
        for (Point a : astroids) {
            losMap.get(a.y).set(a.x, getDetectable(a, astroids).size());
        }
        return losMap;
    }

    private List<Point> getDetectable(Point from, List<Point> astroids) {
        List<Point> detectable = new ArrayList<>();
        for (Point b : astroids) {
            if (!from.equals(b) && canDetect(from, b, astroids)) {
                detectable.add(b);
            }
        }
        return detectable;
    }

    private static Point getLaserPos(List<List<String>> map) {
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(0).size(); x++) {
                if (map.get(y).get(x).equalsIgnoreCase("X")) {
                    return Point.of(x, y);
                }
            }
        }
        return null;
    }

    private List<Point> getAstroids(List<List<String>> map) {
        List<Point> astroids = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map.get(y).get(x).equals("#")) {
                    astroids.add(Point.of(x, y));
                }
            }
        }
        return astroids;
    }

    private boolean canDetect(Point from, Point to, List<Point> astroids) {
        for (Point betweenCandidate : astroids) {
            if (betweenCandidate == to || betweenCandidate == from)
                continue;
            if (isBetween(betweenCandidate, from, to)) {
                return false;
            }
        }
        return true;
    }

    public boolean isBetween(Point between, Point from, Point to) {
        if (dist(from, between) == 0 || dist(to, between) == 0 || dist(from, between) >= dist(from, to)) {
            return false;
        }
        int dxTo = from.x - to.x;
        int dyTo = from.y - to.y;
        int dxCand = from.x - between.x;
        int dyCand = from.y - between.y;
        if (from.x == to.x && from.x == between.x) {
            return Math.abs(dyCand) < Math.abs(dyTo) && sign(dyTo) == sign(dyCand);
        }
        if (from.y == to.y && from.y == between.y) {
            return Math.abs(dxCand) < Math.abs(dxTo) && sign(dxTo) == sign(dxCand);
        }
        double kTo = dxTo * 1.0 / dyTo;
        double kCand = dxCand * 1.0 / dyCand;
        return kTo == kCand && sign(dyTo) == sign(dyCand) && sign(dxTo) == sign(dxCand);
    }

    private int sign(int i) {
        return i > 0 ? 1 : -1;
    }

    public double dist(Point base, Point cand) {
        return Math.sqrt(Math.pow((base.x - cand.x), 2.0) + Math.pow((base.y - cand.y), 2));
    }

    private List<List<Integer>> emptyMap() {
        List<List<Integer>> losMap = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            ArrayList<Integer> line = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                line.add(0);
            }
            losMap.add(line);
        }
        return losMap;
    }

    public static Point findBest(List<List<Integer>> map) {
        int num = 0;
        Point bestSpot = null;
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.size(); x++) {
                if (map.get(y).get(x) > num) {
                    num = map.get(y).get(x);
                    bestSpot = Point.of(x, y);
                }
            }
        }
        return bestSpot;
    }

    @Override
    public String part1() {
        List<List<Integer>> map = detectMap(input);
        Point best = findBest(map);
        return Integer.toString(map.get(best.y).get(best.x));
    }

    @Override
    public String part2() {
        Point shot200 = findShot(200, laserShootMap(input));
        return Integer.toString((shot200.x * 100) + shot200.y);
    }
}
