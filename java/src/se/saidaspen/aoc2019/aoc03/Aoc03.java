package se.saidaspen.aoc2019.aoc03;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.toList;

/**
 * This is the attempted soltuion to Advent of Code 2019 day 3
 * Problem can be found here:
 * https://adventofcode.com/2019/day/3
 */
public class Aoc03 {

    public static final Point ORIGO = new Point(0, 0);
    // This specifies if we are running part 1 or part 2 of the problem
    private static boolean IS_PART1 = false;
    private List<String> lines;

    public Aoc03(List<String> lines) {
        this.lines = lines;
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = lines(Paths.get(args[0])).collect(toList());
        Aoc03 app = new Aoc03(lines);
        if (IS_PART1 == true) {
            Optional<Integer> closestCrossing = app.findClosestCrossing();
            if (closestCrossing.isPresent()) {
                System.out.println(closestCrossing.get());
            } else {
                System.out.println("No crossings found");
            }
        } else {
            
        }
    }

    public Optional<Integer> findClosestCrossing() {
        List<List<Point>> wires = lines.stream().map(l -> l.split(",")).map(Aoc03::toCoordinates).collect(toList());
        List<Point> crossings = findCrossings(wires.get(0), wires.get(1));
        List<Integer> distances = crossings.stream().filter(p -> !p.equals(ORIGO)).map(l -> manhattanDistanceBetween(ORIGO, l)).collect(toList());
        return distances.stream().min(Integer::compareTo);
    }

    private static Integer manhattanDistanceBetween(Point from, Point to) {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    public static List<Point> findCrossings(List<Point> pointsA, List<Point> pointsB) {
        List<Point> crossings = new ArrayList<>();
        for (int i = 0; i < pointsA.size() - 1; i++) {
            for (int j = 0; j < pointsB.size() - 1; j++) {
                Optional<Point> cross = crosses(pointsB.get(j), pointsB.get(j + 1), pointsA.get(i), pointsA.get(i + 1));
                cross.ifPresent(crossings::add);
            }
        }
        return crossings;
    }

    public static Optional<Point> crosses(Point a, Point b, Point c, Point d) {
        Integer crossX = null;
        if (a.x == b.x) { // X compare
            if (((c.y <= a.y && c.y >= b.y) || (c.y >= a.y && c.y <= b.y)) &&
            ((c.x <= a.x && d.x >= a.x) || (c.x >= a.x && d.x <= a.x))){
                return Optional.of(new Point(a.x, c.y));
            }
        } else { // y compare
            if (((c.x <= a.x && c.x >= b.x) || (c.x >= a.x && c.x <= b.x)) &&
            ((c.y <= a.y && d.y >= b.y) || (c.y >= a.y && d.y <= b.y)) ){
                return Optional.of(new Point(c.x, a.y));
            }
        }
        return Optional.empty();
    }

    private static boolean isOnLine(Point c, Point d, Point crossing) {
        return (crossing.x <= d.x && crossing.x >= c.x
                && crossing.y >= c.y && crossing.y <= d.y);
    }

    private static List<Point> toCoordinates(String[] wire) {
        List<Point> points = new ArrayList<>();
        Point currentPoint = ORIGO;
        points.add(currentPoint);
        for (String instr : wire) {
            char direction = instr.charAt(0);
            int magnitude = Integer.parseInt(instr.substring(1));
            if (direction == 'D') {
                currentPoint = new Point(currentPoint.x, currentPoint.y - magnitude);
            } else if (direction == 'R') {
                currentPoint = new Point(currentPoint.x + magnitude, currentPoint.y);
            } else if (direction == 'U') {
                currentPoint = new Point(currentPoint.x, currentPoint.y + magnitude);
            } else if (direction == 'L') {
                currentPoint = new Point(currentPoint.x - magnitude, currentPoint.y);
            }
            points.add(currentPoint);
        }
        return points;
    }

    public static class Point {
        int x, y;

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
