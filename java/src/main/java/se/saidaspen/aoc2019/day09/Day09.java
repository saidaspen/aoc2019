package se.saidaspen.aoc2019.day09;

import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.IntComputer;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Solution to Advent of Code 2019 Day 9
 * The original puzzle can be found here: https://adventofcode.com/2019/day/9
 */
public final class Day09 implements Day {

    private final Long[] code;

    public Day09(String input) {
        code = Arrays.stream(input.split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
    }

    public static void main(String[] args) throws IOException, InterruptedException {


    }

    private Long run(Long[] code, Long i) {
        try {
            ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10000);
            ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
            in.put(i);
            IntComputer amp = new IntComputer(code, in, out);
            amp.run();
            return out.poll();
        } catch (Exception e ){
            throw new RuntimeException("Exception while execution int code", e);
        }
    }

    @Override
    public String part1() {
        return Long.toString(run(code, 1L));
    }

    @Override
    public String part2() {
        return Long.toString(run(code, 2L));
    }
}
