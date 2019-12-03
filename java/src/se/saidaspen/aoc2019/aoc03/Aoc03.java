package se.saidaspen.aoc2019.aoc03;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.toList;

/**
 * This is the attempted solution to Advent of Code 2019 day 3
 * Problem can be found here:
 * https://adventofcode.com/2019/day/3
 */
public class Aoc03 {
    // This specifies if we are running part 1 or part 2 of the problem
    private static boolean IS_PART1 = false;
    private static final Point ORIGIN = new Point(0, 0);

    public static void main(String[] args) throws IOException {
        List<List<Point>> wires = lines(Paths.get(args[0])).collect(toList()).stream()
                .map(l -> l.split(","))
                .map(Aoc03::toCoordinates)
                .collect(toList());
        if (IS_PART1 == true) {
            findClosestCrossing(wires.get(0), wires.get(1)).ifPresent(System.out::println);
        } else {
            findShortestWireLength(wires.get(0), wires.get(1)).ifPresent(System.out::println);
        }
    }

    public static Optional<Integer> findClosestCrossing(List<Point> wire1, List<Point> wire2) {
        return findCrossings(wire1, wire2)
                .stream().filter(p -> !p.equals(ORIGIN))
                .map(ORIGIN::manhattanDistanceTo)
                .collect(toList())
                .stream()
                .min(Integer::compareTo);
    }

    public static Optional<Integer> findShortestWireLength(List<Point> wire1, List<Point> wire2) {
        return findCrossings(wire1, wire2)
                .stream()
                .map(x -> wireLengthTo(wire1, x) + wireLengthTo(wire2, x))
                .min(Integer::compareTo);
    }

    private static int wireLengthTo(List<Point> wire, Point x) {
        int length = 0;
        for (int i = 0; i < wire.size() - 1; i++) {
            if (!x.isOnLine(wire.get(i), wire.get(i + 1))) {
                length += wire.get(i).manhattanDistanceTo(wire.get(i + 1));
            } else {
                length += wire.get(i).manhattanDistanceTo(x);
                break;
            }
        }
        return length;
    }

    private static List<Point> findCrossings(List<Point> wire1, List<Point> wire2) {
        List<Point> crossings = new ArrayList<>();
        for (int i = 0; i < wire1.size() - 1; i++) {
            for (int j = 0; j < wire2.size() - 1; j++) {
                crosses(wire2.get(j), wire2.get(j + 1), wire1.get(i), wire1.get(i + 1)).ifPresent(crossings::add);
            }
        }
        return crossings;
    }

    private static Optional<Point> crosses(Point a, Point b, Point c, Point d) {
        Point candidate = (a.x == b.x) ? new Point(a.x, c.y) : new Point(c.x, a.y);
        return candidate.isOnLine(c, d) && candidate.isOnLine(a, b) ? Optional.of(candidate) : Optional.empty();
    }

    public static List<Point> toCoordinates(String[] wire) {
        Point currentPoint = ORIGIN;
        List<Point> points = new ArrayList<>();
        points.add(currentPoint);
        for (String instr : wire) {
            char dir = instr.charAt(0);
            int steps = Integer.parseInt(instr.substring(1));
            if (dir == 'D') {
                currentPoint = new Point(currentPoint.x, currentPoint.y - steps);
            } else if (dir == 'R') {
                currentPoint = new Point(currentPoint.x + steps, currentPoint.y);
            } else if (dir == 'U') {
                currentPoint = new Point(currentPoint.x, currentPoint.y + steps);
            } else if (dir == 'L') {
                currentPoint = new Point(currentPoint.x - steps, currentPoint.y);
            }
            points.add(currentPoint);
        }
        return points;
    }

}
