package se.saidaspen.aoc2019.day16;

import se.saidaspen.aoc2019.Day;

import java.util.Arrays;

/**
 * Solution to Advent of Code 2019 Day 16
 * The original puzzle can be found here: https://adventofcode.com/2019/day/16
 */
public class Day16 implements Day {

    private static final Integer[] basePattern = new Integer[]{0, 1, 0, -1};
    private final int msgOffset;
    private final int numPhases;
    private int[] data;
    private int inSize;

    Day16(String input, int repeat, int numPhases) {
        msgOffset = Integer.parseInt(input.substring(0, 7));
        this.numPhases = numPhases;
        String repeated = input.repeat(repeat);
        inSize = repeated.length();
        data = new int[inSize];
        for (int i = 0; i < inSize; i++) {
            data[i] = repeated.charAt(i) - '0';
        }
    }

    public String part1() {
        for (int p = 0; p < numPhases; p++) {
            for (int i = 0; i < data.length; i++) {
                data[i] = applyPattern(i);
            }
        }
        return createString(data).substring(0, 8);
    }

    public String part2() {
        int len = data.length - msgOffset;
        assert msgOffset > inSize / 2;
        for (int i = 0; i < numPhases; i++) {
            for (int j = len - 2; j >= 0; j--) {
                data[j + msgOffset] = (data[j + msgOffset] + data[j + msgOffset + 1]) % 10;
            }
        }
        return createString(Arrays.copyOfRange(data, msgOffset, msgOffset + 8));
    }

    private int applyPattern(int times) {
        int val = 0;
        int i = times;
        while (i < data.length) {
            int patternPos = ((i + 1) / (times + 1)) % basePattern.length;
            if (patternPos % 2 == 0) {
                i = i + times + 1;
                continue;
            }
            val += data[i] * basePattern[patternPos];
            i++;
        }
        return Math.abs(val) % 10;
    }

    private String createString(int[] out) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : out) {
            sb.append(i);
        }
        return sb.toString();
    }
}
