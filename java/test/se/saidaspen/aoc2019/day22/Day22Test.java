package se.saidaspen.aoc2019.day22;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class Day22Test {

    public static final BigInteger TEN = BigInteger.valueOf(10);

    @Test
    public void factoryResetAtStart() {
        assertPos(asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), new Day22("", TEN));
    }

    @Test
    public void part1DealIntoNew() {
        assertPos(asList(9, 8, 7, 6, 5, 4, 3, 2, 1, 0), new Day22("deal into new stack", TEN));
    }

    @Test
    public void part1DealIntoNewTwice() {
        Day22 app = new Day22("deal into new stack\ndeal into new stack", TEN);
        assertPos(asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), app);
    }

    @Test
    public void part1Cut3Cards() {
        assertPos(asList(3, 4, 5, 6, 7, 8, 9, 0, 1, 2), new Day22("cut 3", TEN));
    }

    @Test
    public void part1CutNeg4Cards() {
        assertPos(asList(6, 7, 8, 9, 0, 1, 2, 3, 4, 5), new Day22("cut -4", TEN));
    }

    @Test
    public void part1DealWithIncrement() {
        assertPos(asList(0, 7, 4, 1, 8, 5, 2, 9, 6, 3), new Day22("deal with increment 3", TEN));
    }

    private void assertPos(List<Integer> exp, Day22 app) {
        for (int i = 0; i < exp.size(); i++) {
            assertEquals(exp.indexOf(i), app.positionOf(i));
        }
    }

    @Test
    public void part1Example1Part1() {
        assertPos(asList(0, 3, 6, 9, 2, 5, 8, 1, 4, 7), new Day22("deal with increment 7\n", TEN));
    }

    @Test
    public void part1Example1Part1And2() {
        Day22 app = new Day22(
                "deal with increment 7\n"
                +"deal into new stack\n", TEN);
        assertPos(asList(7, 4, 1, 8, 5, 2, 9, 6, 3, 0), app);
    }


    @Test
    public void part1Example1() {
        Day22 app = new Day22("deal with increment 7\n" +
                "deal into new stack\n" +
                "deal into new stack\n", TEN);
        assertPos(asList(0, 3, 6, 9, 2, 5, 8, 1, 4, 7), app);
    }

    @Test
    public void part1Example2() {
        Day22 app = new Day22("cut 6\n" +
                "deal with increment 7\n" +
                "deal into new stack\n", TEN);
        assertPos(asList(3, 0, 7, 4, 1, 8, 5, 2, 9, 6), app);
    }


    @Test
    public void part1Example3() {
        Day22 app = new Day22("deal with increment 7\n" +
                "deal with increment 9\n" +
                "cut -2\n", TEN);
        assertPos(asList(6, 3, 0, 7, 4, 1, 8, 5, 2, 9), app);
    }

    @Test
    public void part1Example4() {
        Day22 app = new Day22(
                "deal into new stack\n" +
                        "cut -2\n" +
                        "deal with increment 7\n" +
                        "cut 8\n" +
                        "cut -4\n" +
                        "deal with increment 7\n" +
                        "cut 3\n" +
                        "deal with increment 9\n" +
                        "deal with increment 3\n" +
                        "cut -1\n", TEN);
        assertPos(asList(9, 2, 5, 8, 1, 4, 7, 0, 3, 6), app);
    }

    @Test
    public void part1() throws Exception {
        Day22 app = new Day22(AocUtil.input(22), 10007);
        assertEquals("1252", app.part1());
    }

    @Test
    public void part2() throws Exception {
        Day22 app = new Day22(AocUtil.input(22), BigInteger.valueOf(119315717514047L), BigInteger.valueOf(101741582076661L));
        assertEquals("46116012647793", app.part2());
    }
}
