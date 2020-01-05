package se.saidaspen.aoc2019.day06;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import static org.junit.Assert.*;

public class Day06Test {

    @Test
    public void part1() throws Exception {
        assertEquals("273985", new Day06(AocUtil.input(6)).part1());
    }

    @Test
    public void part2() throws Exception {
        assertEquals("460", new Day06(AocUtil.input(6)).part2());
    }
}
