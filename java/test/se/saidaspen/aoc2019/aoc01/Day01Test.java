package se.saidaspen.aoc2019.aoc01;

import org.junit.Before;
import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;
import se.saidaspen.aoc2019.StringAnswer;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Day01Test {

    private Day01 app;

    @Before
    public void setup() throws IOException {
        app = new Day01(AocUtil.input(1));
    }

    @Test
    public void part1() throws Exception {
        assertEquals(StringAnswer.of("3278434"), app.part1());
    }

    @Test
    public void part2() throws Exception {
        assertEquals(StringAnswer.of("4914785"), app.part2());
    }
}

