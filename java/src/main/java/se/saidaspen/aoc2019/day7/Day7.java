package se.saidaspen.aoc2019.day7;

import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.IntComputer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.Collections.swap;
import static java.util.stream.Collectors.toList;
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

    private static final int PHASE_MIN = 0;
    private static final int PHASE_MAX = 4;
    private final Long[] code;

    public Day7(String input) {
        code = toLongCode(input);
    }

    @Override
    public String part1() {
        return Long.toString(run(false));
    }

    @Override
    public String part2() {
        return Long.toString(run(true));
    }

    private long run(boolean useFeedback) {
        List<List<Long>> permutations = getPermutations(LongStream.range(PHASE_MIN, PHASE_MAX + 1).boxed().collect(toList()), 0);
        return permutations.parallelStream()
                .mapToLong(i -> findLargestThrust(code, i, useFeedback)).max()
                .orElseThrow(() -> new RuntimeException("Unable to find a maximum value"));
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
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
            List<BlockingQueue<Long>> wires = IntStream.range(0, phases.size() + 1)
                    .mapToObj(c -> new ArrayBlockingQueue<Long>(10_000))
                    .collect(toList());
            if (useFeedback) {
                wires.remove(wires.size() - 1);
                wires.add(wires.size(), wires.get(0));
            }
            List<Amp> amps = new LinkedList<>();
            for (int i = 0; i < phases.size(); i++) {
                wires.get(i).put(phases.get(i));
                //amps.add(new Amp(code, wires.get(i), wires.get(i + 1)));
                executor.execute(new Amp(code, wires.get(i), wires.get(i + 1)));
            }
            wires.get(0).put(0L); // Initial Input
            executor.shutdown();
            executor.awaitTermination(1L, TimeUnit.DAYS);
            return wires.get(wires.size() - 1).poll(10L, TimeUnit.DAYS);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static class Amp implements Runnable {
        public boolean isRunnable() {
            return status == Status.RUNNABLE || (!in.isEmpty() && status == Status.WAITING);
        }

        public enum Status {RUNNABLE, RUNNING, HALTED, WAITING}

        private Status status = Status.RUNNABLE;
        private final Long[] code;
        private int pc = 0;
        private final BlockingQueue<Long> in;
        private final BlockingQueue<Long> out;

        private Amp(Long[] code, BlockingQueue<Long> in, BlockingQueue<Long> out) {
            this.code = Arrays.copyOf(code, code.length);
            this.in = in;
            this.out = out;
        }

        public void run() {
            try {
                do {
                    String cmd = String.format("%05d", code[pc]);
                    int opCode = Integer.parseInt(cmd.substring(3));
                    if (opCode == /*INPUT*/ 3) {
                        status = Status.WAITING;
                        Long inputVal = in.poll(10L, TimeUnit.DAYS);
                        System.out.println("Got input: " + inputVal);
                        code[code[pc + 1].intValue()] = inputVal;
                        status = Status.RUNNABLE;
                        pc += 2;
                        continue;
                    } else if (opCode == /*OUTPUT*/ 4) {
                        long outVal = code[code[pc + 1].intValue()];
                        System.out.println("Sending output: " + outVal);
                        out.put(outVal);
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
                } while (code[pc] != 99);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }
}
