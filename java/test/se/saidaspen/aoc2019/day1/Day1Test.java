package se.saidaspen.aoc2019.day1;

import org.junit.Before;
import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Day1Test {

    private Day1 app;

    @Before
    public void setup() throws IOException {
        app = new Day1(AocUtil.input(1));
    }

    @Test
    public void part1() throws Exception {
        assertEquals("3278434", app.part1());
    }

    @Test
    public void part2() throws Exception {
        assertEquals("4914785", app.part2());
    }
}

