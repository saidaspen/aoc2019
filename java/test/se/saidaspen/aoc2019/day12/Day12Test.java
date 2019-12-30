package se.saidaspen.aoc2019.day12;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import se.saidaspen.aoc2019.AocUtil;

import java.io.IOException;

public final class Day12Test {

    private final String EXAMPLE_1 = "<x=-1, y=0, z=2>\n" +
            "<x=2, y=-10, z=-7>\n" +
            "<x=4, y=-8, z=8>\n" +
            "<x=3, y=5, z=-1>";

    private final String EXAMPLE_2 = "<x=-8, y=-10, z=0>\n" +
            "<x=5, y=5, z=10>\n" +
            "<x=2, y=-7, z=3>\n" +
            "<x=9, y=-8, z=-3>";

    @Test
    public void part1Example1() {
        Day12 app = new Day12(EXAMPLE_1);
        assertEquals(179, app.getSystemEnergy(10));
    }

    @Test
    public void part1Example2() {
        Day12 app = new Day12(EXAMPLE_2);
        assertEquals(1940, app.getSystemEnergy(100));
    }

    @Test
    public void part1Input() throws IOException {
        assertEquals(7202, new Day12(AocUtil.input(12)).getSystemEnergy(1000));
    }

    @Test
    public void part2Example1() {
        Day12 app = new Day12(EXAMPLE_1);
        assertEquals(2772L, app.getSystemPeriod());
    }

    @Test
    public void part2Example2() {
        Day12 app = new Day12(EXAMPLE_2);
        assertEquals(4686774924L, app.getSystemPeriod());
    }

    @Test
    public void part2Input() throws IOException {
        assertEquals(537881600740876L, new Day12(AocUtil.input(12)).getSystemPeriod());
    }
}
