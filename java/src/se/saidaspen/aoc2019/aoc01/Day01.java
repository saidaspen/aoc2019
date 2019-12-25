package se.saidaspen.aoc2019.aoc01;

import se.saidaspen.aoc2019.Answer;
import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.StringAnswer;

import java.util.Arrays;

import static java.lang.Integer.parseInt;
import static java.lang.Math.floor;

public final class Day01 implements Day {

    private final String input;

    public Day01(String input) {
        this.input = input;
    }

    @Override
    public Answer part1() {
        return StringAnswer.of(Integer.toString(Arrays.stream(input.split("\n"))
                .mapToInt(d -> Day01.fuelFor(parseInt(d), false)).sum()));
    }

    @Override
    public Answer part2() {
        return StringAnswer.of(Integer.toString(Arrays.stream(input.split("\n"))
                .mapToInt(d -> Day01.fuelFor(parseInt(d), true)).sum()));
    }

    private static int fuelFor(int d, boolean recursive) {
        int fuelReq = (int) floor((double) d / 3) - 2;
        return fuelReq < 1 ? 0 : !recursive ? fuelReq : fuelReq + fuelFor(fuelReq, true);
    }
}
