package se.saidaspen.aoc2019.day05;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.saidaspen.aoc2019.AocUtil.input;

public class Day05Test {

    @Test
    public void part1() throws Exception {
        assertEquals("4511442", new Day05(input(5)).part1());
    }

    @Test
    public void part2() throws Exception {
        assertEquals("12648139", new Day05(input(5)).part2());
    }
}
