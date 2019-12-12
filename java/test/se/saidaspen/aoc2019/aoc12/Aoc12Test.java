
package se.saidaspen.aoc2019.aoc12;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class Aoc12Test {

    private final String EXAMPLE_1 = "<x=-1, y=0, z=2>\n" +
            "<x=2, y=-10, z=-7>\n" +
            "<x=4, y=-8, z=8>\n" +
            "<x=3, y=5, z=-1>";

    private final String EXAMPLE_2 = "<x=-8, y=-10, z=0>\n" +
            "<x=5, y=5, z=10>\n" +
            "<x=2, y=-7, z=3>\n" +
            "<x=9, y=-8, z=-3>";

    private final String INPUT = "<x=17, y=-9, z=4>\n"+
            "<x=2, y=2, z=-13>\n"+
            "<x=-1, y=5, z=-1>\n"+
            "<x=4, y=7, z=-7>";

    @Test
    public void part1Example1() {
        Aoc12 app = new Aoc12(EXAMPLE_1);
        assertEquals(179, app.getSystemEnergy(10));
    }

    @Test
    public void part1Example2() {
        Aoc12 app = new Aoc12(EXAMPLE_2);
        assertEquals(1940, app.getSystemEnergy(100));
    }

    @Test
    public void part1Input() {
        Aoc12 app = new Aoc12(INPUT);
        assertEquals(7202, app.getSystemEnergy(1000));
    }

    @Test
    public void part2Example1() {
        Aoc12 app = new Aoc12(EXAMPLE_1);
        assertEquals(2772L, app.getSystemPeriod());
    }

    @Test
    public void part2Example2() {
        Aoc12 app = new Aoc12(EXAMPLE_2);
        assertEquals(4686774924L, app.getSystemPeriod());
    }

    @Test
    public void part2Input() {
        Aoc12 app = new Aoc12(INPUT);
        assertEquals(537881600740876L, app.getSystemPeriod());
    }
}
