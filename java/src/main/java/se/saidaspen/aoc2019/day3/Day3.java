package se.saidaspen.aoc2019.day3;

import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Solution for Advent of Code 2019 Day 3
 * The original puzzle can be found here: https://adventofcode.com/2019/day/3
 * <p>
 */
public class Day3 implements Day {
    private static final Point ORIGIN = Point.of(0, 0);
    private final String input;

    public Day3(String input) {
        this.input = input;
    }

    @Override
    public String part1() {
        List<List<Point>> wires = toWires(); // Input always has two wires
        Optional<Integer> crossing = findClosestCrossing(wires.get(0), wires.get(1));
        if (crossing.isEmpty()) {
            throw new RuntimeException("Unable to find crossing");
        }
        return Integer.toString(crossing.get());
    }

    @Override
    public String part2() {
        List<List<Point>> wires = toWires(); // Input always has two wires
        Optional<Integer> length = findShortestWireLength(wires.get(0), wires.get(1));
        if (length.isEmpty()) {
            throw new RuntimeException("Unable to find crossing");
        }
        return Integer.toString(length.get());
    }

    private Optional<Integer> findClosestCrossing(List<Point> wire1, List<Point> wire2) {
        return findCrossings(wire1, wire2)
                .stream().filter(p -> !p.equals(ORIGIN))
                .map(ORIGIN::manhattanDistanceTo)
                .collect(toList())
                .stream()
                .min(Integer::compareTo);
    }

    private List<Point> findCrossings(List<Point> wire1, List<Point> wire2) {
        List<Point> crossings = new ArrayList<>();
        for (int i = 0; i < wire1.size() - 1; i++) {
            for (int j = 0; j < wire2.size() - 1; j++) {
                crosses(wire2.get(j), wire2.get(j + 1), wire1.get(i), wire1.get(i + 1)).ifPresent(crossings::add);
            }
        }
        return crossings;
    }

    public static List<Point> toCoordinates(String[] wire) {
        Point curr = ORIGIN;
        List<Point> points = new ArrayList<>();
        points.add(curr);
        for (String instr : wire) {
            char dir = instr.charAt(0);
            int steps = Integer.parseInt(instr.substring(1));
            if (dir == 'D') {
                curr = Point.of(curr.x, curr.y - steps);
            } else if (dir == 'R') {
                curr = Point.of(curr.x + steps, curr.y);
            } else if (dir == 'U') {
                curr = Point.of(curr.x, curr.y + steps);
            } else if (dir == 'L') {
                curr = Point.of(curr.x - steps, curr.y);
            }
            points.add(curr);
        }
        return points;
    }

    private static Optional<Point> crosses(Point a, Point b, Point c, Point d) {
        Point candidate = (a.x == b.x) ? Point.of(a.x, c.y) : Point.of(c.x, a.y);
        return candidate.isOnLine(c, d) && candidate.isOnLine(a, b) ? Optional.of(candidate) : Optional.empty();
    }

    public Optional<Integer> findShortestWireLength(List<Point> wire1, List<Point> wire2) {
        return findCrossings(wire1, wire2)
                .stream()
                .map(x -> wireLengthTo(wire1, x) + wireLengthTo(wire2, x))
                .min(Integer::compareTo);
    }

    private List<List<Point>> toWires() {
        return Arrays.stream(input.split(System.lineSeparator()))
                .map(l -> l.split(","))
                .map(Day3::toCoordinates)
                .collect(toList());
    }

    private int wireLengthTo(List<Point> wire, Point x) {
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
}
