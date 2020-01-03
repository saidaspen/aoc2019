package se.saidaspen.aoc2019.day21;

import org.junit.Ignore;
import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;
import se.saidaspen.aoc2019.day20.Day20;

import static org.junit.Assert.*;

public class Day21Test {

    @Ignore
    @Test
    public void part1() throws Exception {
        Day21 app = new Day21(AocUtil.input(21));
        assertEquals("19359533", app.part1());
    }

    @Ignore
    @Test
    public void part2() throws Exception {
        Day21 app = new Day21(AocUtil.input(21));
        assertEquals("1140310551", app.part2());
    }

}
