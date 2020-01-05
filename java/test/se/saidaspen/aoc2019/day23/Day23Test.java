package se.saidaspen.aoc2019.day23;

import org.junit.Test;
import static org.junit.Assert.*;
import static se.saidaspen.aoc2019.AocUtil.input;

public class Day23Test {

    @Test
    public void part1() throws Exception {
        assertEquals("23954", new Day23(input(23)).part1());
    }

    @Test
    public void part2() throws Exception {
        assertEquals("17265", new Day23(input(23)).part2());
    }

}
