package se.saidaspen.aoc2019.aoc18;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Aoc18Test {

    @Test
    public void straightTest() {
        String input =
                "#########\n" +
                "#@.....a#\n" +
                "#########";
        assertEquals(6, new Aoc18().part1(input));
    }

    @Test
    public void straightTest2() {
        String input =
                "#########\n" +
                "#....@.a#\n" +
                "#########";
        assertEquals(2, new Aoc18().part1(input));
    }

    @Test
    public void part1Example1() {
        String input =
                "#########\n" +
                "#b.A.@.a#\n" +
                "#########";
        assertEquals(8, new Aoc18().part1(input));
    }

    @Test
    public void part1Example2Simplified() {
        String input =
                //          11111111112222
                //012345678901234567890123
                "########\n" +
                "#bA@aBc#\n" +
                "########\n";
        assertEquals(9, new Aoc18().part1(input));
    }

    @Test
    public void part1Example2() {
        String input =
               //          11111111112222
               //012345678901234567890123
                "########################\n" +
                "#f.D.E.e.C.b.A.@.a.B.c.#\n" +
                "######################.#\n" +
                "#d.....................#\n" +
                "########################\n";
        assertEquals(86, new Aoc18().part1(input));
    }

    @Test
    public void part1Example3() {
        String input =
                "########################\n" +
                "#...............b.C.D.f#\n" +
                "#.######################\n" +
                "#.....@.a.B.c.d.A.e.F.g#\n" +
                "########################\n";
        assertEquals(132, new Aoc18().part1(input));
    }

    @Test
    public void part1Example4() {
        String input =
                        "#################\n" +
                        "#i.G..c...e..H.p#\n" +
                        "########.########\n" +
                        "#j.A..b...f..D.o#\n" +
                        "########@########\n" +
                        "#k.E..a...g..B.n#\n" +
                        "########.########\n" +
                        "#l.F..d...h..C.m#\n" +
                        "#################\n";
        assertEquals(136, new Aoc18().part1(input));
    }


    @Test
    public void part1Example5() {
        String input = "########################\n" +
                "#@..............ac.GI.b#\n" +
                "###d#e#f################\n" +
                "###A#B#C################\n" +
                "###g#h#i################\n" +
                "########################\n";
        assertEquals(81, new Aoc18().part1(input));
    }
}
