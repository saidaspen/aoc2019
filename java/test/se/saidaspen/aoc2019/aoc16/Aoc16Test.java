package se.saidaspen.aoc2019.aoc16;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Aoc16Test {

    @Test
    public void part1Example1() throws Exception {
        String input = "12345678";
        Aoc16 app = new Aoc16(input, 1);
        long before = System.currentTimeMillis();
        String afterPhase1 = app.part1();
        String afterPhase2 = app.part1();
        String afterPhase3 = app.part1();
        String afterPhase4 = app.part1();
        System.out.println(String.format("Took %s ms", System.currentTimeMillis() - before));
        assertEquals("48226158", afterPhase1);
        assertEquals("34040438", afterPhase2);
        assertEquals("03415518", afterPhase3);
        assertEquals("01029498", afterPhase4);
    }

    @Test
    public void part1Example2() throws Exception {
        String input = "80871224585914546619083218645595";
        Aoc16 app = new Aoc16(input, 1);
        String val = input;
        for (int i = 0; i < 100; i++) {
            long before = System.currentTimeMillis();
            val = app.part1();
            System.out.println("Iteration " + i + " took: " + (System.currentTimeMillis() - before) + "ms");
        }
        assertEquals("24176176", val.substring(0, 8));
    }

    @Test
    public void part1Example3() throws Exception {
        String input = "19617804207202209144916044189917";
        Aoc16 app = new Aoc16(input, 1);
        String val = input;
        for (int i = 0; i < 100; i++) {
            long before = System.currentTimeMillis();
            val = app.part1();
            System.out.println("Iteration " + i + " took: " + (System.currentTimeMillis() - before) + "ms");
        }
        assertEquals("73745418", val.substring(0, 8));
    }

    @Test
    public void part1Example4() throws Exception {
        String input = "69317163492948606335995924319873";
        Aoc16 app = new Aoc16(input, 1);
        String val = input;
        for (int i = 0; i < 100; i++) {
            long before = System.currentTimeMillis();
            val = app.part1();
            System.out.println("Iteration " + i + " took: " + (System.currentTimeMillis() - before) + "ms");
        }
        assertEquals("52432133", val.substring(0, 8));
    }

    @Test
    public void part2Example1() throws Exception {
        String input = "03036732577212944063491565474664";
        Aoc16 app = new Aoc16(input, 10_000);
        assertEquals("84462026", app.part2());
    }

    @Test
    public void part2Example2() throws Exception {
        String input = "02935109699940807407585447034323";
        Aoc16 app = new Aoc16(input, 10_000);
        assertEquals("78725270", app.part2());
    }

    @Test
    public void part2Example3() throws Exception {
        String input = "03081770884921959731165446850517";
        Aoc16 app = new Aoc16(input, 10_000);
        assertEquals("53553731", app.part2());
    }
}
