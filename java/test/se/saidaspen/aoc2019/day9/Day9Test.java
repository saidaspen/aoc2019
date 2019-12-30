package se.saidaspen.aoc2019.day9;

import org.junit.Test;
import se.saidaspen.aoc2019.IntComputer;
import se.saidaspen.aoc2019.day8.Day8;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static se.saidaspen.aoc2019.AocUtil.input;

@SuppressWarnings("ConstantConditions")
public class Day9Test {

    @Test
    public void part1() throws IOException {
        assertEquals("2171728567", new Day9(input(9)).part1());
    }

    @Test
    public void part2() throws IOException {
        assertEquals("49815", new Day9(input(9)).part2());
    }

    @Test
    public void test_aoc05_1_less_than8() throws Exception {
        String input = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99";
        Long[] code = Arrays.stream(input.split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10);
        in.put(7L);
        IntComputer cpu = new IntComputer(code, in, out);
        cpu.run();
        assertEquals(999L, (long) out.poll(1, TimeUnit.DAYS));
    }

    @Test
    public void test_aoc05_1_8() throws Exception {
        String input = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99";
        Long[] code = Arrays.stream(input.split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10);
        in.put(8L);
        IntComputer cpu = new IntComputer(code, in, out);
        cpu.run();
        assertEquals(1000L, (long) out.poll(1, TimeUnit.DAYS));
    }

    @Test
    public void test_aoc05_1_morethan8() throws Exception {
        String input = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99";
        Long[] code = Arrays.stream(input.split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10);
        in.put(9L);
        IntComputer cpu = new IntComputer(code, in, out);
        cpu.run();
        assertEquals(1001L, (long) out.poll(1, TimeUnit.DAYS));
    }

    @Test
    public void aoc09_test1() throws Exception {
        String input = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99";
        Long[] code = Arrays.stream(input.split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(1000);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(1000);
        IntComputer cpu = new IntComputer(code, in, out);
        cpu.run();
        assertEquals(109L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(1L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(204L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(-1L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(1001L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(100L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(1L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(100L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(1008L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(100L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(16L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(101L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(1006L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(101L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(0L, (long) out.poll(1, TimeUnit.DAYS));
        assertEquals(99L, (long) out.poll(1, TimeUnit.DAYS));
    }

    @Test
    public void aoc09_test2() throws Exception {
        String input = "1102,34915192,34915192,7,4,7,99,0";
        Long[] code = Arrays.stream(input.split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10);
        IntComputer cpu = new IntComputer(code, in, out);
        cpu.run();
        assertEquals(16, Long.toString(out.poll(1, TimeUnit.DAYS)).length());
    }

    @Test
    public void aoc09_test3() throws Exception {
        String input = "104,1125899906842624,99";
        Long[] code = Arrays.stream(input.split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10);
        IntComputer cpu = new IntComputer(code, in, out);
        cpu.run();
        assertEquals(1125899906842624L, (long) out.poll(1, TimeUnit.DAYS));
    }

}
