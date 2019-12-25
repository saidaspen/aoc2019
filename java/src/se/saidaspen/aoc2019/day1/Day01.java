package se.saidaspen.aoc2019.day1;

import se.saidaspen.aoc2019.Answer;
import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.StringAnswer;

import java.util.Arrays;

import static java.lang.Integer.parseInt;
import static java.lang.Math.floor;

/**
 * Solution for Advent of Code 2019 Day 1
 * The original puzzle can be found here: https://adventofcode.com/2019/day/1
 *
 * As far as I understand, this is the standard rocket equation.
 * In Part 1 we want to figure out the fuel required for a certain weight.
 * In Part 2, we also need to take into account the weight for the fuel itself, resulting
 * in a recursive call to figure out the fuel requirement.
 *
 * Here are my results:
 * Part     Time        Place
 * 1        00:02:33    407
 * 2        00:12:29    823
 *
 */
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
