package se.saidaspen.aoc2019.day16;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import static org.junit.Assert.assertEquals;

public class Day16Test {

    @Test
    public void part1Example1() {
        String input = "12345678";
        Day16 app = new Day16(input, 1, 4);
        long before = System.currentTimeMillis();
        String afterPhase4 = app.part1();
        System.out.println(String.format("Took %s ms", System.currentTimeMillis() - before));
        assertEquals("01029498", afterPhase4);
    }

    @Test
    public void part1Example2() {
        String input = "80871224585914546619083218645595";
        Day16 app = new Day16(input, 1, 100);
        String val = app.part1();
        assertEquals("24176176", val);
    }

    @Test
    public void part1Example3() {
        String input = "19617804207202209144916044189917";
        Day16 app = new Day16(input, 1, 100);
        assertEquals("73745418", app.part1());
    }

    @Test
    public void part1Example4() {
        String input = "69317163492948606335995924319873";
        Day16 app = new Day16(input, 1, 100);
        assertEquals("52432133", app.part1());
    }

    @Test
    public void part2Example1() {
        String input = "03036732577212944063491565474664";
        Day16 app = new Day16(input, 10_000, 100);
        assertEquals("84462026", app.part2());
    }

    @Test
    public void part2Example2() {
        String input = "02935109699940807407585447034323";
        Day16 app = new Day16(input, 10_000, 100);
        assertEquals("78725270", app.part2());
    }

    @Test
    public void part2Example3() {
        String input = "03081770884921959731165446850517";
        Day16 app = new Day16(input, 10_000, 100);
        assertEquals("53553731", app.part2());
    }

    @Test
    public void part1() throws Exception {
        String input = AocUtil.input(16);
        Day16 app = new Day16(input, 1, 100);
        assertEquals("45834272", app.part1());
    }

    @Test
    public void part2() throws Exception {
        String input = AocUtil.input(16);
        Day16 app = new Day16(input, 10_000, 100);
        assertEquals("37615297", app.part2());
    }
}
