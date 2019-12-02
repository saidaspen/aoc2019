package se.saidaspen.aoc2019.aoc01;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

/**
 * This is the attempted soltuion to Advent of Code 2019 day 2
 * Problem can be found here:
 * https://adventofcode.com/2019/day/2
 */
public class Aoc02 {

    // This specifies if we are running part 1 or part 2 of the problem
    private static boolean IS_PART1 = false;

    // EXPECTED_OUTPUT is a constant given in the puzzle itself. It differs from person to person.
    public static final int EXPECTED_OUTPUT = 19690720;

    private static final class Params {
        int first, second;

        public Params(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "Params{first=" + first + ", second=" + second + '}';
        }
    }

    public static void main(String[] args) throws IOException {
        String lines = Files.readString(Paths.get(args[0]), Charset.defaultCharset());
        Integer[] opCodes = Arrays.stream(lines.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
        if (IS_PART1) {
            opCodes[1] = 12;
            opCodes[2] = 2;
            System.out.println(runProgram(opCodes));
        } else {
            Optional<Params> params = findParams(opCodes, EXPECTED_OUTPUT);
            if (params.isEmpty()) {
                System.err.println("Unable to find input parameters");
                System.exit(1);
            }
            System.out.println(100 * params.get().first + params.get().second);
        }
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

    private static Integer runProgram(Integer[] input) {
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
            }
            readPos = readPos + 4;
            opCode = opCodes[readPos];
        }
        return opCodes[0];
    }
}
