package se.saidaspen.aoc2019.day11;

import org.junit.Test;
import se.saidaspen.aoc2019.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static se.saidaspen.aoc2019.AocUtil.input;

public class Day11Test {

    @Test
    public void part1() throws IOException {
        assertEquals("2293", new Day11(input(11)).part1());
    }

    @Test
    public void part2() throws IOException {
        assertEquals(
                "░░██░░█░░█░█░░░░░██░░███░░███░░░██░░█░░░░░░\n" +
                "░█░░█░█░░█░█░░░░█░░█░█░░█░█░░█░█░░█░█░░░░░░\n" +
                "░█░░█░████░█░░░░█░░░░█░░█░█░░█░█░░█░█░░░░░░\n" +
                "░████░█░░█░█░░░░█░░░░███░░███░░████░█░░░░░░\n" +
                "░█░░█░█░░█░█░░░░█░░█░█░░░░█░█░░█░░█░█░░░░░░\n" +
                "░█░░█░█░░█░████░░██░░█░░░░█░░█░█░░█░████░░░\n",
                new Day11(input(11)).part2());
    }

}
