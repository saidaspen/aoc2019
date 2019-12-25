package se.saidaspen.aoc2019.aoc04;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import static org.junit.Assert.*;

public class Aoc04Test {


    @Test
    public void part1() throws Exception {
        assertEquals("1864", new Aoc04(AocUtil.input(4)).part1());
    }

    @Test
    public void part2() throws Exception {
        assertEquals("1258", new Aoc04(AocUtil.input(4)).part2());
    }

    @Test
    public void part1Examples() {
        assertTrue(Aoc04.meetsCriteria(111111, true));
        assertFalse(Aoc04.meetsCriteria(223450, true));
        assertFalse(Aoc04.meetsCriteria(123789, true));
        assertTrue(Aoc04.meetsCriteria(112233, true));
        assertTrue(Aoc04.meetsCriteria(123444, true));
        assertTrue(Aoc04.meetsCriteria(111122, true));
    }

    @Test
    public void test3() {
        assertFalse(Aoc04.meetsCriteria(137778, false));
    }

    @Test
    public void part2Examples() {
        assertTrue(Aoc04.meetsCriteria(112233, false));
        assertFalse(Aoc04.meetsCriteria(123444, false));
        assertTrue(Aoc04.meetsCriteria(111122, true));
    }

}
