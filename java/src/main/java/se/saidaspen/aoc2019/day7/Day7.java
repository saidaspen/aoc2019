package se.saidaspen.aoc2019.day7;

import se.saidaspen.aoc2019.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.Collections.swap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.LongStream.rangeClosed;
import static se.saidaspen.aoc2019.AocUtil.toLongCode;

/**
 * Solution to Advent of Code 2019 Day 7
 * The original puzzle can be found here: https://adventofcode.com/2019/day/4
 * <p>
 * Here is the accompanying poem:
 * <p>
 * Fuel it up, if ye may,
 * Rocket equation was essential.
 * Navigation was cleared yesterday,
 * Amplification is now exponential.
 * <p>
 * Turn it up and loop it back,
 * get that thrust to amplify
 * a trick, a hack — let's call it that
 * tonight we are off – tonight we fly
 */

public class Day7 implements Day {

    private final Long[] code;

    public Day7(String input) {
        code = toLongCode(input);
    }

    @Override
    public String part1() {
        List<List<Long>> permutations = getPermutations(rangeClosed(0, 4).boxed().collect(toList()), 0);
        return Long.toString(permutations.parallelStream()
                .mapToLong(i -> findLargestThrust(code, i, false)).max()
                .orElseThrow(() -> new RuntimeException("Unable to find a maximum value")));
    }

    @Override
    public String part2() {
        List<List<Long>> permutations = getPermutations(rangeClosed(5, 9).boxed().collect(toList()), 0);
        return Long.toString(permutations.parallelStream()
                .mapToLong(i -> findLargestThrust(code, i, true)).max()
                .orElseThrow(() -> new RuntimeException("Unable to find a maximum value")));
    }

    private static List<List<Long>> getPermutations(List<Long> arr, int k) {
        ArrayList<List<Long>> permutations = new ArrayList<>();
        for (int i = k; i < arr.size(); i++) {
            swap(arr, i, k);
            permutations.addAll(getPermutations(arr, k + 1));
            swap(arr, k, i);
        }
        if (k == arr.size() - 1) {
            permutations.add(new ArrayList<>(arr));
        }
        return permutations;
    }

    public static Long findLargestThrust(Long[] code, List<Long> phases, boolean useFeedback) {
        try {
            List<BlockingQueue<Long>> wires = rangeClosed(0, phases.size())
                    .mapToObj(c -> new ArrayBlockingQueue<Long>(10_000))
                    .collect(toList());
            if (useFeedback) {
                wires.remove(wires.size() - 1);
                wires.add(wires.size(), wires.get(0));
            }
            List<Amp> amps = new LinkedList<>();
            for (int i = 0; i < phases.size(); i++) {
                wires.get(i).put(phases.get(i));
                amps.add(new Amp(code, wires.get(i), wires.get(i + 1)));
            }
            wires.get(0).put(0L); // Initial Input
            boolean allDone = false;
            while (!allDone) {
                allDone = true;
                for (Amp amp : amps) {
                    if (amp.isRunnable()) {
                        allDone = false;
                        amp.run();
                    }
                }
            }
            return amps.get(4).lastSent;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static class Amp {

        public enum Status {RUNNABLE, HALTED, WAITING}
        private Status status = Status.RUNNABLE;
        private final Long[] code;
        private Long lastSent = 0L;
        private int pc = 0;
        private final BlockingQueue<Long> in;
        private final BlockingQueue<Long> out;

        private Amp(Long[] code, BlockingQueue<Long> in, BlockingQueue<Long> out) {
            this.code = Arrays.copyOf(code, code.length);
            this.in = in;
            this.out = out;
        }

        public boolean isRunnable() {
            return status == Status.RUNNABLE || (!in.isEmpty() && status == Status.WAITING);
        }

        public void run() {
            try {
                while (true) {
                    String cmd = String.format("%05d", code[pc]);
                    int opCode = Integer.parseInt(cmd.substring(3));
                    if (opCode == /*HALT*/ 99) {
                        status = Status.HALTED;
                        return;
                    }
                    if (opCode == /*INPUT*/ 3) {
                        Long inputVal = in.poll();
                        if (inputVal == null) {
                            status = Status.WAITING;
                            return;
                        }
                        status = Status.RUNNABLE;
                        code[code[pc + 1].intValue()] = inputVal;
                        pc += 2;
                        continue;
                    } else if (opCode == /*OUTPUT*/ 4) {
                        long outVal = code[code[pc + 1].intValue()];
                        out.put(outVal);
                        lastSent = outVal;
                        pc += 2;
                        continue;
                    }
                    Long param1 = cmd.charAt(2) == '0' ? code[code[pc + 1].intValue()] : code[pc + 1];
                    long param2 = cmd.charAt(1) == '0' ? code[code[pc + 2].intValue()] : code[pc + 2];
                    if (opCode == /*ADD*/ 1) {
                        code[code[pc + 3].intValue()] = param1 + param2;
                        pc += 4;
                    } else if (opCode == /*MULT*/ 2) {
                        code[code[pc + 3].intValue()] = param1 * param2;
                        pc += 4;
                    } else if (opCode == /*JMP_IF_TRUE*/ 5) {
                        pc = param1 > 0 ? (int) param2 : pc + 3;
                    } else if (opCode == /*JMP_IF_FALSE*/ 6) {
                        pc = param1 == 0 ? (int) param2 : pc + 3;
                    } else if (opCode == /*LESS_THAN*/ 7) {
                        code[code[pc + 3].intValue()] = param1 < param2 ? 1L : 0L;
                        pc += 4;
                    } else if (opCode == /* EQUAL*/ 8) {
                        code[code[pc + 3].intValue()] = param1 == param2 ? 1L : 0L;
                        pc += 4;
                    }
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }
}
