package se.saidaspen.aoc2019.aoc10;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Aoc10Test {

    @Test
    public void distanceBetween() {
        assertTrue(0.001 > Math.abs(1.0 - new Aoc10(10, 10).dist(new Coord(0, 0), new Coord(1, 0))));
        assertTrue(0.001 > Math.abs(2.0 - new Aoc10(10, 10).dist(new Coord(0, 0), new Coord(2, 0))));
        assertTrue(0.001 > Math.abs(3.0 - new Aoc10(10, 10).dist(new Coord(0, 0), new Coord(3, 0))));
        assertTrue(2 > new Aoc10(10, 10).dist(new Coord(0, 0), new Coord(1, 1)));
    }

    @Test
    public void notBetweenIfFurtherAway() {
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(2, 0), new Coord(0, 0), new Coord(1, 0)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(3, 0), new Coord(0, 0), new Coord(1, 0)));
    }

    @Test
    public void notBetweenOneself() {
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(0, 0), new Coord(0, 0), new Coord(0, 1)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(1, 1), new Coord(1, 1), new Coord(0, 1)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(0, 0), new Coord(0, 0), new Coord(1, 2)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(1, 1), new Coord(0, 0), new Coord(1, 1)));
    }

    @Test
    public void betweenDiagonal() {
        assertFalse(new Aoc10(20, 20).isBetween(new Coord(2, 2), new Coord(0, 0), new Coord(1, 1)));
        assertFalse(new Aoc10(20, 20).isBetween(new Coord(3, 3), new Coord(0, 0), new Coord(1, 1)));
        assertFalse(new Aoc10(20, 20).isBetween(new Coord(4, 4), new Coord(0, 0), new Coord(2, 2)));
        assertFalse(new Aoc10(20, 20).isBetween(new Coord(9, 9), new Coord(0, 0), new Coord(3, 3)));
        //  F . .
        //  . T .
        //  . . X
        assertFalse(new Aoc10(3, 3).isBetween(new Coord(2, 2), new Coord(0, 0), new Coord(1, 1)));
        assertFalse(new Aoc10(5, 5).isBetween(new Coord(4, 3), new Coord(3, 2), new Coord(1, 0)));
        // F . .
        // . X .
        // . . T
        assertTrue(new Aoc10(10, 10).isBetween(new Coord(1, 1), new Coord(0, 0), new Coord(2, 2)));
        // F . . . .
        // . . x . .
        // . . . . T
        assertTrue(new Aoc10(10, 10).isBetween(new Coord(2, 2), new Coord(0, 0), new Coord(4, 4)));
        // T . . . .
        // . . x . .
        // . . . . F
        assertTrue(new Aoc10(3, 3).isBetween(new Coord(2, 2), new Coord(4, 4), new Coord(0, 0)));
        assertFalse(new Aoc10(5, 5).isBetween(new Coord(4, 3), new Coord(4, 2), new Coord(4, 0)));
    }

    @Test
    public void betweenSameX() {
        assertTrue(new Aoc10(10, 10).isBetween(new Coord(1, 0), new Coord(0, 0), new Coord(2, 0)));
        assertTrue(new Aoc10(10, 10).isBetween(new Coord(2, 0), new Coord(0, 0), new Coord(3, 0)));

        // . F X T .
        assertTrue(new Aoc10(1, 5).isBetween(new Coord(2, 0), new Coord(1, 0), new Coord(3, 0)));
    }

    @Test
    public void betweenSameY() {
        assertTrue(new Aoc10(10, 10).isBetween(new Coord(0, 1), new Coord(0, 0), new Coord(0, 2)));
        assertTrue(new Aoc10(10, 10).isBetween(new Coord(0, 2), new Coord(0, 0), new Coord(0, 3)));
    }

    @Test
    public void betweenGiven1() {
        // .T..#
        // .....
        // ##X##
        // ....#
        // ...F#
        assertTrue(new Aoc10(10, 10).isBetween(new Coord(2, 2), new Coord(3, 4), new Coord(1, 0)));
        assertTrue(new Aoc10(10, 10).isBetween(new Coord(2, 2), new Coord(1, 0), new Coord(3, 4)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(4, 0), new Coord(3, 4), new Coord(1, 0)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(0, 2), new Coord(3, 4), new Coord(1, 0)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(1, 2), new Coord(3, 4), new Coord(1, 0)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(3, 2), new Coord(3, 4), new Coord(1, 0)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(4, 2), new Coord(3, 4), new Coord(1, 0)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(4, 3), new Coord(3, 4), new Coord(1, 0)));
        assertFalse(new Aoc10(10, 10).isBetween(new Coord(4, 4), new Coord(3, 4), new Coord(1, 0)));
    }

    @Test
    public void testDetectMapGiven0() {
        //#.........
        //...A......
        //...B..a...
        //.EDCG....a
        //..F.c.b...
        //.....c....
        //..efd.c.gb
        //.......c..
        //....f...c.
        /*A*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(3, 1), new Coord(0, 0), new Coord(6, 2)));
        /*A*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(3, 1), new Coord(0, 0), new Coord(9, 3)));
        /*B*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(3, 2), new Coord(0, 0), new Coord(6, 4)));
        /*B*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(3, 2), new Coord(0, 0), new Coord(9, 6)));
        /*C*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(3, 3), new Coord(0, 0), new Coord(4, 4)));
        /*C*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(3, 3), new Coord(0, 0), new Coord(5, 5)));
        /*C*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(3, 3), new Coord(0, 0), new Coord(6, 6)));
        /*C*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(3, 3), new Coord(0, 0), new Coord(7, 7)));
        /*C*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(3, 3), new Coord(0, 0), new Coord(8, 8)));
        /*E*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(1, 3), new Coord(0, 0), new Coord(2, 6)));
        /*F*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(2, 4), new Coord(0, 0), new Coord(3, 6)));
        /*F*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(2, 4), new Coord(0, 0), new Coord(4, 8)));
        /*F*/
        assertTrue(new Aoc10(9, 10).isBetween(new Coord(4, 3), new Coord(0, 0), new Coord(8, 6)));
    }

    @Test
    public void testDetectMapGiven1() {
        // GIVEN   EXPECTED
        // .#..#    .7..7
        // .....    .....
        // #####    67775
        // ....#    ....7
        // ...##    ...87
        String input = ".#..#\n" +
                ".....\n" +
                "#####\n" +
                "....#\n" +
                "...##";
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(asList(0, 7, 0, 0, 7));
        expected.add(asList(0, 0, 0, 0, 0));
        expected.add(asList(6, 7, 7, 7, 5));
        expected.add(asList(0, 0, 0, 0, 7));
        expected.add(asList(0, 0, 0, 8, 7));
        List<List<Integer>> map = Aoc10.detectMap(input);
        for (int row = 0; row < expected.size(); row++) {
            for (int col = 0; col < expected.get(0).size(); col++) {
                assertEquals(String.format("Error for (%s, %s)", col, row), expected.get(row).get(col), map.get(row).get(col));
            }
        }
    }

    @Test
    public void testDetectMapGiven2() {
        String input = "......#.#.\n" +
                "#..#.#....\n" +
                "..#######.\n" +
                ".#.#.###..\n" +
                ".#..#.....\n" +
                "..#....#.#\n" +
                "#..#....#.\n" +
                ".##.#..###\n" +
                "##...#..#.\n" +
                ".#....####";
        assertBestValue(input, 33);
    }

    @Test
    public void testDetectMapGiven3() {
        String input = "#.#...#.#.\n" +
                ".###....#.\n" +
                ".#....#...\n" +
                "##.#.#.#.#\n" +
                "....#.#.#.\n" +
                ".##..###.#\n" +
                "..#...##..\n" +
                "..##....##\n" +
                "......#...\n" +
                ".####.###.";
        assertBestValue(input, 35);
    }

    @Test
    public void testDetectMapGiven4() {
        String input = ".#..#..###\n" +
                "####.###.#\n" +
                "....###.#.\n" +
                "..###.##.#\n" +
                "##.##.#.#.\n" +
                "....###..#\n" +
                "..#.#..#.#\n" +
                "#..#.#.###\n" +
                ".##...##.#\n" +
                ".....#.#..";
        assertBestValue(input, 41);
    }

    @Test
    public void testDetectMapGiven5() {
        String input = ".#..##.###...#######\n" +
                "##.############..##.\n" +
                ".#.######.########.#\n" +
                ".###.#######.####.#.\n" +
                "#####.##.#.##.###.##\n" +
                "..#####..#.#########\n" +
                "####################\n" +
                "#.####....###.#.#.##\n" +
                "##.#################\n" +
                "#####.##.###..####..\n" +
                "..######..##.#######\n" +
                "####.##.####...##..#\n" +
                ".#####..#.######.###\n" +
                "##...#.##########...\n" +
                "#.##########.#######\n" +
                ".####.#.###.###.#.##\n" +
                "....##.##.###..#####\n" +
                ".#.#.###########.###\n" +
                "#.#.#.#####.####.###\n" +
                "###.##.####.##.#..##";
        assertBestValue(input, 210);
    }

    @Test
    public void testLaserMapGiven0() {
        String input = ".#....#####...#..\n" +
                "##...##.#####..##\n" +
                "##...#...#.#####.\n" +
                "..#.....X...###..\n" +
                "..#.#.....#....##";
        List<List<Integer>> map = Aoc10.laserShootMap(input);
        assertEquals(new Coord(8, 1), Aoc10.findShot(1, map));
        assertEquals(new Coord(9, 0), Aoc10.findShot(2, map));
        assertEquals(new Coord(9, 1), Aoc10.findShot(3, map));
        assertEquals(new Coord(10, 0), Aoc10.findShot(4, map));
        assertEquals(new Coord(9, 2), Aoc10.findShot(5, map));
        assertEquals(new Coord(11, 1), Aoc10.findShot(6, map));
        assertEquals(new Coord(12, 1), Aoc10.findShot(7, map));
        assertEquals(new Coord(11, 2), Aoc10.findShot(8, map));
        assertEquals(new Coord(15, 1), Aoc10.findShot(9, map));

        assertEquals(new Coord(12, 2), Aoc10.findShot(9 + 1, map));
        assertEquals(new Coord(13, 2), Aoc10.findShot(9 + 2, map));
        assertEquals(new Coord(14, 2), Aoc10.findShot(9 + 3, map));
        assertEquals(new Coord(15, 2), Aoc10.findShot(9 + 4, map));
        assertEquals(new Coord(12, 3), Aoc10.findShot(9 + 5, map));
        assertEquals(new Coord(16, 4), Aoc10.findShot(9 + 6, map));
        assertEquals(new Coord(15, 4), Aoc10.findShot(9 + 7, map));
        assertEquals(new Coord(10, 4), Aoc10.findShot(9 + 8, map));
        assertEquals(new Coord(4, 4), Aoc10.findShot(9 + 9, map));
    }

    @Test
    public void laserShotExampleBig() {
        String input = ".#..##.###...#######\n" +
                "##.############..##.\n" +
                ".#.######.########.#\n" +
                ".###.#######.####.#.\n" +
                "#####.##.#.##.###.##\n" +
                "..#####..#.#########\n" +
                "####################\n" +
                "#.####....###.#.#.##\n" +
                "##.#################\n" +
                "#####.##.###..####..\n" +
                "..######..##.#######\n" +
                "####.##.####...##..#\n" +
                ".#####..#.######.###\n" +
                "##...#.##########...\n" +
                "#.##########.#######\n" +
                ".####.#.###.###.#.##\n" +
                "....##.##.###..#####\n" +
                ".#.#.###########.###\n" +
                "#.#.#.#####.####.###\n" +
                "###.##.####.##.#..##";
        List<List<Integer>> map = Aoc10.laserShootMap(input);
        assertEquals(new Coord(11, 12), Aoc10.findShot(1, map));
        assertEquals(new Coord(12, 1), Aoc10.findShot(2, map));
        assertEquals(new Coord(12, 2), Aoc10.findShot(3, map));
        assertEquals(new Coord(12, 8), Aoc10.findShot(10, map));
        assertEquals(new Coord(16, 0), Aoc10.findShot(20, map));
        assertEquals(new Coord(16, 9), Aoc10.findShot(50, map));
        assertEquals(new Coord(10, 16), Aoc10.findShot(100, map));
        assertEquals(new Coord(9, 6), Aoc10.findShot(199, map));
        assertEquals(new Coord(8, 2), Aoc10.findShot(200, map));
        assertEquals(new Coord(10, 9), Aoc10.findShot(201, map));
        assertEquals(new Coord(11, 1), Aoc10.findShot(299, map));
    }

    @Test
    public void angleOf() {
        Coord x = new Coord(2, 2);
        Coord a = new Coord(2, 0);
        Coord b = new Coord(2, 1);
        Coord c = new Coord(2, 4);
        Coord d = new Coord(2, 3);
        Coord e = new Coord(4, 2);
        Coord f = new Coord(0, 2);
        Coord g = new Coord(4, 4);
        Coord h = new Coord(0, 4);
        Coord i = new Coord(0, 0);
        Coord j = new Coord(4, 0);
        // i . a . j
        // . . b . .
        // f . x . e
        // . . d . .
        // h . c . g
        assertEquals("", 0.0, Aoc10.angleOf(x, a), 0.0);
        assertEquals("", 0.0, Aoc10.angleOf(x, b), 0.0);
        assertEquals(Math.PI, Aoc10.angleOf(x, c), 0.1);
        assertEquals(Math.PI, Aoc10.angleOf(x, d), 0.1);
        assertEquals(Math.PI / 2, Aoc10.angleOf(x, e), 0.1);
        assertEquals(3 * Math.PI / 2, Aoc10.angleOf(x, f), 0.1);
        assertEquals(3 * Math.PI / 2 + Math.PI / 4, Aoc10.angleOf(x, i), 0.1);
        assertEquals(Math.PI / 4, Aoc10.angleOf(x, j), 0.1);

        assertEquals(Math.PI / 2 + Math.PI / 4, Aoc10.angleOf(x, g), 0.1);
        assertEquals(2 * Math.PI / 2 + Math.PI / 4, Aoc10.angleOf(x, h), 0.1);
    }

    private void assertBestValue(String input, int value) {
        List<List<Integer>> map = Aoc10.detectMap(input);
        Coord best = Aoc10.findBest(map);
        assertEquals(new Integer(value), map.get(best.y).get(best.x));
    }
}
