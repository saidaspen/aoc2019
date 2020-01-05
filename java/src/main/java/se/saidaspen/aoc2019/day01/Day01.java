package se.saidaspen.aoc2019.day01;

import se.saidaspen.aoc2019.Day;

import java.util.Arrays;

import static java.lang.Integer.parseInt;
import static java.lang.Math.floor;

/**
 * Solution for Advent of Code 2019 Day 1
 * The original puzzle can be found here: https://adventofcode.com/2019/day/1
 * <p>
 * As far as I understand, this is the standard rocket equation.
 * In Part 1 we want to figure out the fuel required for a certain weight.
 * In Part 2, we also need to take into account the weight for the fuel itself, resulting
 * in a recursive call to figure out the fuel requirement.
 */
public final class Day01 implements Day {

    private final String input;

    public Day01(String input) {
        this.input = input;
    }

    @Override
    public String part1() {
        return Integer.toString(Arrays.stream(input.split("\n"))
                .mapToInt(d -> fuelFor(parseInt(d), false)).sum());
    }

    @Override
    public String part2() {
        return Integer.toString(Arrays.stream(input.split("\n"))
                .mapToInt(d -> fuelFor(parseInt(d), true)).sum());
    }

    private static int fuelFor(int d, boolean inclFuelWeight) {
        int fuelReq = (int) floor((double) d / 3) - 2;
        return fuelReq < 1 ? 0 : !inclFuelWeight ? fuelReq : fuelReq + fuelFor(fuelReq, true);
    }
}
