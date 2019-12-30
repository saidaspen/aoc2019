package se.saidaspen.aoc2019.day8;

import org.junit.Test;
import se.saidaspen.aoc2019.day7.Day7;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static se.saidaspen.aoc2019.AocUtil.input;

public class Day8Test {

    @Test
    public void part1() throws IOException {
        assertEquals("2375", new Day8(input(8)).part1());
    }

    @Test
    public void part2() throws IOException {
        assertEquals(
                " #  #  #        #        #     #        #     #  #  #        #           # \n" +
                " #        #     #     #        #        #     #        #     #           # \n" +
                " #        #     #  #           #  #  #  #     #        #        #     #    \n" +
                " #  #  #        #     #        #        #     #  #  #              #       \n" +
                " #     #        #     #        #        #     #     #              #       \n" +
                " #        #     #        #     #        #     #        #           #       \n",
                new Day8(input(8)).part2());
    }

}
