import org.junit.Test;
import se.saidaspen.aoc2019.aoc03.Aoc03;

import java.util.List;

import static org.junit.Assert.*;

public class Aoc03Test {

    @Test
    public void testCrosses() {
        Aoc03.Point a = new Aoc03.Point(0, 0);
        Aoc03.Point b = new Aoc03.Point(2, 0);
        Aoc03.Point c = new Aoc03.Point(1, -1);
        Aoc03.Point d = new Aoc03.Point(1, 1);
        assertEquals(Aoc03.crosses(a, b, c, d).get(), new Aoc03.Point(1, 0));
    }

    @Test
    public void doesNotCrossIfNotOnLine() {
        Aoc03.Point a = new Aoc03.Point(0, 0);
        Aoc03.Point b = new Aoc03.Point(2, 0);
        Aoc03.Point c = new Aoc03.Point(1, -5);
        Aoc03.Point d = new Aoc03.Point(1, -1);
        assertTrue(Aoc03.crosses(a, b, c, d).isEmpty());
    }

    @Test
    public void crosses() {
        Aoc03.Point a = new Aoc03.Point(6, 7);
        Aoc03.Point b = new Aoc03.Point(6, 3);
        Aoc03.Point c = new Aoc03.Point(8, 5);
        Aoc03.Point d = new Aoc03.Point(3, 5);
        assertEquals(Aoc03.crosses(a, b, c, d).get(), new Aoc03.Point(6, 5));
    }

    @Test
    public void givenTestA() {
        String line1 = "R8,U5,L5,D3";
        String line2 = "U7,R6,D4,L4";
        List<String> lines = List.of(line1, line2);
        Aoc03 app = new Aoc03(lines);
        assertEquals(app.findClosestCrossing().get().intValue(), 6);
    }

    @Test
    public void givenTestB() {
        String line1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
        String line2 = "U62,R66,U55,R34,D71,R55,D58,R83";
        List<String> lines = List.of(line1, line2);
        Aoc03 app = new Aoc03(lines);
        assertEquals(app.findClosestCrossing().get().intValue(), 159);
    }

    @Test
    public void givenTestC() {
        String line1 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
        String line2 = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
        List<String> lines = List.of(line1, line2);
        Aoc03 app = new Aoc03(lines);
        assertEquals(app.findClosestCrossing().get().intValue(), 135);
    }

}