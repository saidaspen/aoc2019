package se.saidaspen.aoc2019.day7;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static se.saidaspen.aoc2019.AocUtil.input;

public class Day7Test {

    @Test
    public void part1() throws IOException {
        assertEquals("262086", new Day7(input(7)).part1());
    }

    @Test
    public void part2() throws IOException {
        assertEquals("5371621", new Day7(input(7)).part2());
    }

}
