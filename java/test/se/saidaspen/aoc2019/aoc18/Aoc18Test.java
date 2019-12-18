package se.saidaspen.aoc2019.aoc18;

import static org.junit.Assert.*;

import org.junit.Test;

public class Aoc18Test {

    @Test
    public void part1Example1() {
        String input = "#########\n" +
                "#b.A.@.a#\n" +
                "#########";
        Aoc18 app = new Aoc18(input, false, 10_000);
        assertEquals(8, app.part1());
    }

    @Test
    public void part1Example2() {
        String input =
                "########################\n" +
                        "#f.D.E.e.C.b.A.@.a.B.c.#\n" +
                        "######################.#\n" +
                        "#d.....................#\n" +
                        "########################";
        Aoc18 app = new Aoc18(input, false, 10_000);
        assertEquals(86, app.part1());
    }

    @Test
    public void part1Example3() {
        String input =
                "########################\n" +
                        "#...............b.C.D.f#\n" +
                        "#.######################\n" +
                        "#.....@.a.B.c.d.A.e.F.g#\n" +
                        "########################";
        Aoc18 app = new Aoc18(input, false, 10_000);
        assertEquals(132, app.part1());
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
                        "#################";
        Aoc18 app = new Aoc18(input, false, 5_000);
        assertEquals(136, app.part1());
    }


    @Test
    public void part1Example5() {
        String input = "########################\n" +
                "#@..............ac.GI.b#\n" +
                "###d#e#f################\n" +
                "###A#B#C################\n" +
                "###g#h#i################\n" +
                "########################";
        Aoc18 app = new Aoc18(input, false, 10_000);
        assertEquals(81, app.part1());
    }

}
