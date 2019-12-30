package se.saidaspen.aoc2019.day17;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;
import se.saidaspen.aoc2019.day16.Day16;

import static org.junit.Assert.*;

public class Day17Test {

    @Test
    public void part1() throws Exception {
        Day17 app = new Day17(AocUtil.input(17));
        assertEquals("1544", app.part1());
    }

    @Test
    public void part2() throws Exception {
        Day17 app = new Day17(AocUtil.input(17));
        assertEquals("696373", app.part2());
    }

}
