package se.saidaspen.aoc2019.aoc24;

import se.saidaspen.aoc2019.MapUtil;
import se.saidaspen.aoc2019.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public final class Aoc24 {

    private String input;
    private Map<Point, Character> map = new HashMap<>();
    private Map<Point3D, Character> map3D = new HashMap<>();


    public static void main(String[] args) throws IOException, InterruptedException {
        Aoc24 app = new Aoc24(new String(Files.readAllBytes(Paths.get(args[0]))));
        //app.part1();
        //app.part2();
        app.part2_2();
    }

    void part2_2() throws InterruptedException {
        List<String> lines = Arrays.stream(input.split("\n")).collect(Collectors.toList());
        for (int i = 0; i < lines.size(); i++) {
            char[] line = lines.get(i).toCharArray();
            for (int col = 0; col < line.length; col++) {
                Point3D p = Point3D.of(col, i, 0);
                map3D.put(p, line[col]);
            }
        }
        int largestX = 0;
        int largestY = 0;
        for (Point3D p : map3D.keySet()) {
            largestX = Math.max(p.x, largestX);
            largestY = Math.max(p.y, largestY);
        }

        int iterations = 200;
        for (int i = 0; i < iterations; i++) {
            HashMap<Point3D, Character> nextMap = new HashMap<>();
            for (int level = -500; level <= 500; level++) {
                for (int row = 0; row <= largestY; row++) {
                    for (int col = 0; col <= largestX; col++) {
                        Point3D p = Point3D.of(row, col, level);
                        List<Point3D> adjacent = p.getAdjacent();
                        long bugsAround = 0;
                        for (Point3D adj : adjacent) {
                            if (adj != null) {
                                if (map3D.containsKey(adj) && map3D.get(adj).equals('#')) {
                                    bugsAround += 1;
                                }
                            }
                        }
                        if (p.x == 2 && p.y == 2 ){
                            continue;
                        }
                        Character c = map3D.get(p);
                        if (c == null || c.equals('.')) {
                            if (bugsAround == 1 || bugsAround == 2) {
                                nextMap.put(p, '#');
                            } else {
                                nextMap.put(p, '.');
                            }
                        } else if (c.equals('#')) {
                            if (bugsAround != 1) {
                                nextMap.put(p, '.');
                            } else {
                                nextMap.put(p, '#');
                            }
                        }
                    }
                }
            }
            map3D = nextMap;
            System.out.println(String.format("numBugs is %s after iteration %s", numBugs(map3D), i));
        }
        //System.out.println(toString3D(map3D));
        System.out.println(String.format("numBugs is %s after iteration %s", numBugs(map3D), iterations));
    }

    private String toString3D(Map<Point3D, Character> map3D) {
        System.out.print("\033[2J"); // Clear screen
        int largestX = 0;
        int largestY = 0;
        int largestZ = 0;
        for (Point3D p : map3D.keySet()) {
            largestX = Math.max(p.x, largestX);
            largestY = Math.max(p.y, largestY);
        }
        List<List<List<Character>>> lines = emptyLines(largestX, largestY, largestZ);
        for (Point3D p : map3D.keySet()) {
            lines.get(p.z).get(p.y).set(p.x, map3D.get(p));
        }
        return render(lines);
    }

    private String render(List<List<List<Character>>> levels) {
        StringBuilder sb = new StringBuilder();
        for (List<List<Character>> lines : levels) {
            for (List<Character> row : lines) {
                for (Character c : row) {
                    sb.append(c);
                }
                sb.append("\n");
            }
            sb.append("\n-----------------\n");
        }
        return sb.toString();

    }

    private List<List<List<Character>>> emptyLines(int xMax, int yMax, int zMax) {
        List<List<List<Character>>> levels = new ArrayList<>();
        for (int level = 0; level <= zMax; level++) {
            List<List<Character>> l = new ArrayList<>();
            for (int row = 0; row <= yMax; row++) {
                List<Character> r = new ArrayList<>();
                for (int col = 0; col <= xMax; col++) {
                    r.add(' ');
                }
                l.add(r);
            }
            levels.add(l);
        }
        return levels;
    }

    Aoc24(String input) {
        this.input = input;
    }


    @SuppressWarnings("unused")
    void part1() throws InterruptedException {
        List<String> lines = Arrays.stream(input.split("\n")).collect(Collectors.toList());
        for (int i = 0; i < lines.size(); i++) {
            char[] line = lines.get(i).toCharArray();
            for (int col = 0; col < line.length; col++) {
                Point p = Point.of(col, i);
                map.put(Point.of(col, i), line[col]);
            }
        }
        List<String> layouts = new ArrayList<>();
        int iteration = 0;
        while (true) {
            iteration++;
            String layout = MapUtil.toString(map);
            if (layouts.contains(layout)) {
                System.out.println(layout);
                int idx = layouts.indexOf(layout);
                System.out.println(String.format("Biodiversity is %s  for iteration %s, which is same as for it %s ", bioDiversity(map), iteration, idx + 1));
                return;
            }
            layouts.add(layout);
            HashMap<Point, Character> nextMap = new HashMap<>();
            for (int row = 0; row < lines.size(); row++) {
                for (int col = 0; col < lines.get(0).length(); col++) {
                    Point p = Point.of(row, col);
                    Point[] adjacent = p.getAdjacent();
                    long bugsAround = 0;
                    for (Point adj : adjacent) {
                        if (adj != null) {
                            if (map.containsKey(adj) && map.get(adj).equals('#')) {
                                bugsAround += 1;
                            }
                        }
                    }
                    char c = map.get(p);
                    if (c == '#') {
                        if (bugsAround != 1) {
                            nextMap.put(p, '.');
                        } else {
                            nextMap.put(p, '#');
                        }
                    } else if (c == '.') {
                        if (bugsAround == 1 || bugsAround == 2) {
                            nextMap.put(p, '#');
                        } else {
                            nextMap.put(p, '.');
                        }
                    }
                }
            }
            map = nextMap;
        }
    }

    private Long totBioDiversity(Map<Integer, Map<Point, Character>> prevMap) {
        long totalBioDiv = 0;
        for (Map<Point, Character> m : prevMap.values()) {
            totalBioDiv += bioDiversity(m);
        }
        return totalBioDiv;
    }

    private String mToString(Map<Integer, Map<Point, Character>> prevMap) {
        StringBuilder sb = new StringBuilder();
        for (Map<Point, Character> m : prevMap.values()) {
            String s = MapUtil.toString(m);
            sb.append(s);
            sb.append('\n');
        }
        return sb.toString();
    }

    private long bugsAround(Point p, Map<Integer, Map<Point, Character>> prevMap, int level) {
        Point[] adjacent = p.getAdjacent();
        long bugsAround = 0;
        for (Point adj : adjacent) {
            if (adj != null) {
                if (prevMap.get(level).containsKey(adj) && prevMap.get(level).get(adj).equals('#')) {
                    bugsAround += 1;
                }
            }
        }
        return bugsAround;
    }

    public static long numBugs(Map<Point3D, Character> map) {
        int largestX = 0;
        int largestY = 0;
        int largestZ = 0;
        int minZ = Integer.MAX_VALUE;
        for (Point3D p : map.keySet()) {
            largestX = Math.max(p.x, largestX);
            largestY = Math.max(p.y, largestY);
            largestZ = Math.max(p.z, largestZ);
            minZ = Math.min(p.z, minZ);
        }
        long bugs = 0;
        for (int level = minZ; level <= largestZ; level++) {
            for (int row = 0; row <= largestY; row++) {
                for (int col = 0; col <= largestX; col++) {
                    if (map.containsKey(Point3D.of(col, row, level)) && map.get(Point3D.of(col, row, level)).equals('#')) {
                        bugs++;
                    }
                }
            }
        }
        return bugs;
    }

    public static long bioDiversity(Map<Point, Character> map) {
        int largestX = 0;
        int largestY = 0;
        for (Point p : map.keySet()) {
            largestX = Math.max(p.x, largestX);
            largestY = Math.max(p.y, largestY);
        }
        long bioPoints = 0;
        int idx = 0;
        for (int row = 0; row <= largestY; row++) {
            for (int col = 0; col <= largestX; col++) {
                if (map.containsKey(Point.of(col, row)) && map.get(Point.of(col, row)).equals('#')) {
                    bioPoints += Math.pow(2.0, idx);
                }
                idx++;
            }
        }
        return bioPoints;
    }
}
