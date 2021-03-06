package se.saidaspen.aoc2019.day04;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import static org.junit.Assert.*;

public class Day04Test {


    @Test
    public void part1() throws Exception {
        assertEquals("1864", new Day04(AocUtil.input(4)).part1());
    }

    @Test
    public void part2() throws Exception {
        assertEquals("1258", new Day04(AocUtil.input(4)).part2());
    }

    @Test
    public void part1Examples() {
        assertTrue(Day04.meetsCriteria(111111, true));
        assertFalse(Day04.meetsCriteria(223450, true));
        assertFalse(Day04.meetsCriteria(123789, true));
        assertTrue(Day04.meetsCriteria(112233, true));
        assertTrue(Day04.meetsCriteria(123444, true));
        assertTrue(Day04.meetsCriteria(111122, true));
    }

    @Test
    public void test3() {
        assertFalse(Day04.meetsCriteria(137778, false));
    }

    @Test
    public void part2Examples() {
        assertTrue(Day04.meetsCriteria(112233, false));
        assertFalse(Day04.meetsCriteria(123444, false));
        assertTrue(Day04.meetsCriteria(111122, true));
    }

}
