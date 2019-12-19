package se.saidaspen.aoc2019.aoc0;

import org.junit.Test;
import se.saidaspen.aoc2019.Point;
import se.saidaspen.aoc2019.aoc03.Aoc03;

import java.util.List;

import static org.junit.Assert.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class Aoc03Test {

    @Test
    public void testDistanceToCrossing1() {
        String l1 = "R8,U5,L5,D3";
        String l2 = "U7,R6,D4,L4";
        assertEquals(Aoc03.findClosestCrossing(wireOf(l1), wireOf(l2)).get().intValue(), 6);
    }

    @Test
    public void testDistanceToCrossing2() {
        String l1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
        String l2 = "U62,R66,U55,R34,D71,R55,D58,R83";
        assertEquals(Aoc03.findClosestCrossing(wireOf(l1), wireOf(l2)).get().intValue(), 159);
    }

    @Test
    public void testDistanceToCrossing3() {
        String l1 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
        String l2 = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
        assertEquals(Aoc03.findClosestCrossing(wireOf(l1), wireOf(l2)).get().intValue(), 135);
    }

    @Test
    public void givenTestWireLength1() {
        String l1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
        String l2 = "U62,R66,U55,R34,D71,R55,D58,R83";
        assertEquals(Aoc03.findShortestWireLength(wireOf(l1), wireOf(l2)).get().intValue(), 610);
    }

    @Test
    public void givenTestWireLength2() {
        String l1 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
        String l2 = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
        assertEquals(Aoc03.findShortestWireLength(wireOf(l1), wireOf(l2)).get().intValue(), 410);
    }

    private List<Point> wireOf(String p1) {
        return Aoc03.toCoordinates(p1.split(","));
    }
}
