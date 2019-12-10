package se.saidaspen.aoc2019.aoc10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Aoc10 {

    private int width;
    private int height;

    public Aoc10(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        List<List<Integer>> map = detectMap(input);
        Coord best = findBest(map);
        System.out.println(String.format("Best spot is %s with value:%s", best, map.get(best.y).get(best.x)));
        Coord shot200 = findShot(200, laserShootMap(input));
        System.out.println(String.format("Part 2: %s", (shot200.x * 100) + shot200.y));
    }

    public static Coord findShot(int i, List<List<Integer>> laserShootMap) {
        for (int row = 0; row < laserShootMap.size(); row++) {
            for (int col = 0; col < laserShootMap.get(0).size(); col++) {
                if (laserShootMap.get(row).get(col) == i) {
                    return new Coord(col, row);
                }
            }
        }
        throw new RuntimeException("Unable to find shot " + i);
    }

    public static List<List<Integer>> laserShootMap(String input) {
        List<List<String>> map = mapOf(input);
        Coord laserPos = getLaserPos(map);
        if (laserPos == null) {
            List<List<Integer>> detectMap = detectMap(input);
            laserPos = findBest(detectMap);
        }
        int height = map.size();
        int width = map.get(0).size();
        return new Aoc10(height, width).laser(laserPos, map);
    }

    public static List<List<Integer>> detectMap(String input) {
        List<List<String>> map = mapOf(input);
        int height = map.size();
        int width = map.get(0).size();
        return new Aoc10(height, width).detect(map);
    }

    private static List<List<String>> mapOf(String input) {
        List<String> lines = Arrays.stream(input.split("\n")).collect(Collectors.toList());
        return lines.stream()
                .map(l -> l.chars().mapToObj(e -> (char) e).map(Object::toString).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public List<List<Integer>> laser(Coord laserPos, List<List<String>> map) {
        List<Coord> astroids = getAstroids(map);
        List<List<Integer>> laserMap = emptyMap();
        int i = 1;
        while (!astroids.isEmpty()) {
            astroids.remove(laserPos);
            List<Coord> detectableAstroids = getDetectable(laserPos, astroids);
            detectableAstroids = detectableAstroids.stream()
                    .sorted(Comparator.comparingDouble(a -> angleOf(laserPos, a)))
                    .collect(Collectors.toList());
            for (Coord a : detectableAstroids) {
                astroids.remove(a);
                laserMap.get(a.y).set(a.x, i++);
            }
        }
        return laserMap;
    }

    public static double angleOf(Coord center, Coord to) {
        double radians = Math.atan2((to.y - center.y) * 1.0, (to.x - center.x) * 1.0);
        radians = (radians + 2 * Math.PI) % (2 * Math.PI); // Make positive
        return (radians + Math.PI / 2) % (2 * Math.PI); // Translate to our coordinates
    }

    public List<List<Integer>> detect(List<List<String>> map) {
        List<Coord> astroids = getAstroids(map);
        List<List<Integer>> losMap = emptyMap();
        for (Coord a : astroids) {
            losMap.get(a.y).set(a.x, getDetectable(a, astroids).size());
        }
        return losMap;
    }

    private List<Coord> getDetectable(Coord from, List<Coord> astroids) {
        List<Coord> detectable = new ArrayList<>();
        for (Coord b : astroids) {
            if (!from.equals(b) && canDetect(from, b, astroids)) {
                detectable.add(b);
            }
        }
        return detectable;
    }

    private static Coord getLaserPos(List<List<String>> map) {
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(0).size(); x++) {
                if (map.get(y).get(x).equalsIgnoreCase("X")) {
                    return new Coord(x, y);
                }
            }
        }
        return null;
    }

    private List<Coord> getAstroids(List<List<String>> map) {
        List<Coord> astroids = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map.get(y).get(x).equals("#")) {
                    astroids.add(new Coord(x, y));
                }
            }
        }
        return astroids;
    }

    private boolean canDetect(Coord from, Coord to, List<Coord> astroids) {
        for (Coord betweenCandidate : astroids) {
            if (betweenCandidate == to || betweenCandidate == from)
                continue;
            if (isBetween(betweenCandidate, from, to)) {
                return false;
            }
        }
        return true;
    }

    public boolean isBetween(Coord between, Coord from, Coord to) {
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

    public double dist(Coord base, Coord cand) {
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

    public static Coord findBest(List<List<Integer>> map) {
        int num = 0;
        Coord bestSpot = null;
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.size(); x++) {
                if (map.get(y).get(x) > num) {
                    num = map.get(y).get(x);
                    bestSpot = new Coord(x, y);
                }
            }
        }
        return bestSpot;
    }
}
