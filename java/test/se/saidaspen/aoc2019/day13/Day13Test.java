package se.saidaspen.aoc2019.day13;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;
import se.saidaspen.aoc2019.day8.Day8;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static se.saidaspen.aoc2019.AocUtil.input;

public final class Day13Test {

    @Test
    public void part1() throws IOException {
        assertEquals("205", new Day13(input(13)).part1());
    }

    @Test
    public void part2() throws IOException {
        assertEquals("10292", new Day13(input(13)).part2());
    }
}
