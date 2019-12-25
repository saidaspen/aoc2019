package se.saidaspen.aoc2019.day2;

import org.junit.Assert;
import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import static java.lang.Integer.valueOf;
import static org.junit.Assert.assertEquals;
import static se.saidaspen.aoc2019.AocUtil.toCode;
import static se.saidaspen.aoc2019.day2.Day2.runProgram;

public class Day2Test {

    @Test
    public void part1() throws Exception {
        Assert.assertEquals("3058646", new Day2(AocUtil.input(2)).part1());
    }

    @Test
    public void part1Example1() {
        assertEquals(valueOf(3500), runProgram(toCode("1,9,10,3,2,3,11,0,99,30,40,50")));
    }

    @Test
    public void part1Examples() {
        assertEquals(valueOf(2), runProgram(toCode("1,0,0,0,99")));
        assertEquals(valueOf(2), runProgram(toCode("2,3,0,3,99")));
        assertEquals(valueOf(2), runProgram(toCode("2,4,4,5,99,0")));
        assertEquals(valueOf(30), runProgram(toCode("1,1,1,4,99,5,6,0,99")));
    }

    @Test
    public void part2() throws Exception {
        assertEquals("8976", new Day2(AocUtil.input(2)).part2());
    }
}

