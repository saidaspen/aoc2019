package se.saidaspen.aoc2019.aoc04;

import org.junit.Test;

import static org.junit.Assert.*;

public class Aoc04Test {

    @Test
    public void test() {
       assertTrue(Aoc04.meetCriteria(112233));
    }

    @Test
    public void test1() {
        assertFalse(Aoc04.meetCriteria(123444));
    }
    @Test
    public void test2() {
        assertTrue(Aoc04.meetCriteria(111122));
    }

    @Test
    public void test3() {
        assertFalse(Aoc04.meetCriteria(137778));
    }

}
