package se.saidaspen.aoc2019.day22;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import java.math.BigInteger;
import java.util.List;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class Day22Test {

    public static final BigInteger TEN = valueOf(10);

    @Test
    public void factoryResetAtStart() {
        assertPos(asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), new Day22("", TEN, ONE));
    }

    @Test
    public void part1DealIntoNew() {
        assertPos(asList(9, 8, 7, 6, 5, 4, 3, 2, 1, 0), new Day22("deal into new stack", TEN, ONE));
    }

    @Test
    public void part1Cut3Cards() {
        assertPos(asList(3, 4, 5, 6, 7, 8, 9, 0, 1, 2), new Day22("cut 3", TEN, ONE));
    }

    @Test
    public void part1DealIntoNewTwice() {
        Day22 app = new Day22("deal into new stack\ndeal into new stack", TEN, ONE);
        assertPos(asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), app);
    }

    @Test
    public void part1CutNeg4Cards() {
        assertPos(asList(6, 7, 8, 9, 0, 1, 2, 3, 4, 5), new Day22("cut -4", TEN, ONE));
    }

    @Test
    public void part1DealWithIncrement() {
        assertPos(asList(0, 7, 4, 1, 8, 5, 2, 9, 6, 3), new Day22("deal with increment 3", TEN, ONE));
    }

    private void assertPos(List<Integer> exp, Day22 app) {
        for (int i = 0; i < exp.size(); i++) {
            assertEquals("Asserting pos of", valueOf(exp.indexOf(i)), app.positionOf(valueOf(i)));
            assertEquals("Asserting value", valueOf(exp.get(i)), app.cardAt(valueOf(i)));
        }
    }

    @Test
    public void part1Example1Part1() {
        assertPos(asList(0, 3, 6, 9, 2, 5, 8, 1, 4, 7), new Day22("deal with increment 7\n", TEN, ONE));
    }

    @Test
    public void part1Example1Part1And2() {
        Day22 app = new Day22(
                "deal with increment 7\n"
                +"deal into new stack\n", TEN, ONE);
        assertPos(asList(7, 4, 1, 8, 5, 2, 9, 6, 3, 0), app);
    }


    @Test
    public void part1Example1() {
        Day22 app = new Day22("deal with increment 7\n" +
                "deal into new stack\n" +
                "deal into new stack\n", TEN, ONE);
        assertPos(asList(0, 3, 6, 9, 2, 5, 8, 1, 4, 7), app);
    }

    @Test
    public void part1Example2() {
        Day22 app = new Day22("cut 6\n" +
                "deal with increment 7\n" +
                "deal into new stack\n", TEN, ONE);
        assertPos(asList(3, 0, 7, 4, 1, 8, 5, 2, 9, 6), app);
    }


    @Test
    public void part1Example3() {
        Day22 app = new Day22("deal with increment 7\n" +
                "deal with increment 9\n" +
                "cut -2\n", TEN, ONE);
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
                        "cut -1\n", TEN, ONE);
        assertPos(asList(9, 2, 5, 8, 1, 4, 7, 0, 3, 6), app);
    }

    @Test
    public void part1() throws Exception {
        Day22 app = new Day22(AocUtil.input(22), valueOf(10007), ONE);
        assertEquals("1252", app.part1());
    }

    @Test
    public void part2() throws Exception {
        Day22 app = new Day22(AocUtil.input(22), valueOf(119315717514047L), valueOf(101741582076661L));
        assertEquals("46116012647793", app.part2());
    }
}
