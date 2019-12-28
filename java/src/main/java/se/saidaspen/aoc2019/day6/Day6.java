package se.saidaspen.aoc2019.day6;

import se.saidaspen.aoc2019.Day;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Solution for Advent of Code 2019 Day 6
 * The original puzzle can be found here: https://adventofcode.com/2019/day/6
 * <p>
 * Poem posted to https://www.reddit.com/r/adventofcode
 * <p>
 * Round and round and round we go
 * even Santa gets dizzy in his chapeau
 * lost in space, no way home?
 * I guess, for now, we must roam.
 * Look at the map, it must be right
 * A christmas without Santa, imagine the sight
 * Map is ready! Space-time is curved
 * here we come, our circles cannot be disturbed!
 */
public final class Day6 implements Day {

    private final Map<String, String> orbits;

    Day6(String input) {
        orbits = Arrays.stream(input.split("\n")).map(l -> l.split("\\)")).collect(toMap(l -> l[1], l -> l[0]));
    }

    @Override
    public String part1() {
        int cnt = orbits.keySet().parallelStream().map(this::pathToCOM).mapToInt(List::size).sum();
        return Integer.toString(cnt);
    }

    @Override
    public String part2() {
        LinkedList<String> youPath = pathToCOM("YOU");
        LinkedList<String> sanPath = pathToCOM("SAN");
        while (youPath.getLast().equals(sanPath.getLast())) {
            youPath.removeLast();
            sanPath.removeLast();
        }
        return Integer.toString(youPath.size() + sanPath.size());
    }

    private LinkedList<String> pathToCOM(String from) {
        String inner = from;
        LinkedList<String> path = new LinkedList<>();
        while ((inner = orbits.get(inner)) != null) {
            path.add(inner);
        }
        return path;
    }
}
