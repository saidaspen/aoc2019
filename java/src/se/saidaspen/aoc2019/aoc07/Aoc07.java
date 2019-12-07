package se.saidaspen.aoc2019.aoc07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.Collections.swap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.LongStream.range;

/**
 * Solution to Advent of Code 2019 Day 7
 *
 * Here is the accompanying poem:
 *
 * Fuel it up, if ye may,
 * Rocket equation was essential.
 * Navigation was cleared yesterday,
 * Amplification is now exponential.
 *
 * Turn it up and loop it back,
 * get that thrust to amplify
 * a trick, a hack — let's call it that
 * tonight we are off – tonight we fly
 *
 */
public class Aoc07 {

    private static final long PHASE_MIN = 5L;
    private static final long PHASE_MAX = 9L;
    private static final boolean USE_FEEDBACK = true;

    public static void main(String[] args) throws IOException {
        Long[] code = Arrays.stream(new String(Files.readAllBytes(Paths.get(args[0]))).split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        List<List<Long>> permutations = getPermutations(range(PHASE_MIN, PHASE_MAX + 1).boxed().collect(toList()), 0);
        System.out.println("Maximum thrust: " + permutations.parallelStream()
                .mapToLong(l -> findLargestThrust(code, l)).max()
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

    private static Long findLargestThrust(Long[] code, List<Long> phases) {
        try {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
            List<BlockingQueue<Long>> wires = IntStream.range(0, phases.size() + 1)
                    .mapToObj(c -> new ArrayBlockingQueue<Long>(2))
                    .collect(toList());
            if (USE_FEEDBACK) {
                wires.remove(wires.size() - 1);
                wires.add(wires.size(), wires.get(0));
            }
            for (int i = 0; i < phases.size(); i++) {
                wires.get(i).put(phases.get(i));
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
                        code[code[pc + 1].intValue()] = in.poll(10L, TimeUnit.DAYS);
                        pc += 2;
                        continue;
                    } else if (opCode == /*OUTPUT*/ 4) {
                        long outVal = code[code[pc + 1].intValue()];
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
