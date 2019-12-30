package se.saidaspen.aoc2019.day15;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static se.saidaspen.aoc2019.AocUtil.input;

public final class Day15Test {

    @Test
    public void part1() throws IOException {
        assertEquals("366", new Day15(input(15)).part1());
    }

    @Test
    public void part2() throws IOException {
        assertEquals("384", new Day15(input(15)).part2());
    }
}
