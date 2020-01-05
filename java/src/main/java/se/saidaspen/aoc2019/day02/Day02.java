package se.saidaspen.aoc2019.day02;

import se.saidaspen.aoc2019.Day;

import java.util.Arrays;
import java.util.Optional;

import static se.saidaspen.aoc2019.AocUtil.toCode;

/**
 * Solution for Advent of Code 2019 Day 2
 * The original puzzle can be found here: https://adventofcode.com/2019/day/2
 */
public class Day02 implements Day {

    private final String input;

    public Day02(String input) {
        this.input = input;
    }

    private static final class Params {
        int first, second;

        private Params(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "Params{first=" + first + ", second=" + second + '}';
        }
    }

    @Override
    public String part1() {
        Integer[] opCodes = toCode(input);
        opCodes[1] = 12;
        opCodes[2] = 2;
        return Integer.toString(runProgram(opCodes));
    }

    @Override
    public String part2() {
        Integer[] opCodes = toCode(input);
        Optional<Params> params = findParams(opCodes, 19690720); // This constant is given in the puzzle description.
        if (params.isEmpty()) {
            throw new RuntimeException("Unable to find input parameters.");
        }
        return Integer.toString(100 * params.get().first + params.get().second);
    }

    @SuppressWarnings("SameParameterValue")
    private static Optional<Params> findParams(Integer[] input, int expected) {
        Integer[] opCodes = Arrays.copyOf(input, input.length);
        for (int param1 = 0; param1 < 99; param1++) {
            for (int param2 = 0; param2 < 99; param2++) {
                opCodes[1] = param1;
                opCodes[2] = param2;
                if (runProgram(opCodes) == expected) {
                    return Optional.of(new Params(param1, param2));
                }
            }
        }
        return Optional.empty(); // Unable to find suitable parameters
    }

    public static Integer runProgram(Integer[] input) {
        Integer[] opCodes = Arrays.copyOf(input, input.length);
        int readPos = 0;
        int opCode = opCodes[readPos];
        while (opCode != 99) {
            Integer operand1 = opCodes[opCodes[readPos + 1]];
            Integer operand2 = opCodes[opCodes[readPos + 2]];
            Integer storePos = opCodes[readPos + 3];
            if (opCode == 1) { // Add
                opCodes[storePos] = operand1 + operand2;
            } else if (opCode == 2) { // Multiply
                opCodes[storePos] = operand1 * operand2;
            } else {
                throw new RuntimeException("Unsupporterd opcode " + opCode);
            }
            readPos = readPos + 4;
            opCode = opCodes[readPos];
        }
        return opCodes[0];
    }
}
