package se.saidaspen.aoc2019.day3;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Day3Test {

    public static final String EXAMPLE1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72\nU62,R66,U55,R34,D71,R55,D58,R83";
    public static final String EXAMPLE_2 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\nU98,R91,D20,R16,D67,R40,U7,R15,U6,R7";

    @Test
    public void part1() throws IOException {
        assertEquals("1285", new Day3(AocUtil.input(3)).part1());
    }

    @Test
    public void part2() throws IOException {
        assertEquals("14228", new Day3(AocUtil.input(3)).part2());
    }

    @Test
    public void testDistanceToCrossing1() {
        assertEquals("6", new Day3("R8,U5,L5,D3\nU7,R6,D4,L4").part1());
    }

    @Test
    public void testDistanceToCrossing2() {
        assertEquals("159", new Day3(EXAMPLE1).part1());
    }

    @Test
    public void testDistanceToCrossing3() {
        assertEquals("135", new Day3(EXAMPLE_2).part1());
    }

    @Test
    public void givenTestWireLength1() {
        assertEquals("610", new Day3(EXAMPLE1).part2());
    }

    @Test
    public void givenTestWireLength2() {
        assertEquals("410", new Day3(EXAMPLE_2).part2());
    }
}
