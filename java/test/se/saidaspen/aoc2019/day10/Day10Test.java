package se.saidaspen.aoc2019.day10;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.saidaspen.aoc2019.AocUtil.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import se.saidaspen.aoc2019.Point;
import se.saidaspen.aoc2019.day9.Day9;

public class Day10Test {

    @Test
    public void part1() throws IOException {
        assertEquals("286", new Day10(input(10)).part1());
    }

    @Test
    public void part2() throws IOException {
        assertEquals("504", new Day10(input(10)).part2());
    }

    @Test
    public void distanceBetween() {
        assertTrue(0.001 > Math.abs(1.0 - new Day10(10, 10).dist(Point.of(0, 0), Point.of(1, 0))));
        assertTrue(0.001 > Math.abs(2.0 - new Day10(10, 10).dist(Point.of(0, 0), Point.of(2, 0))));
        assertTrue(0.001 > Math.abs(3.0 - new Day10(10, 10).dist(Point.of(0, 0), Point.of(3, 0))));
        assertTrue(2 > new Day10(10, 10).dist(Point.of(0, 0), Point.of(1, 1)));
    }

    @Test
    public void notBetweenIfFurtherAway() {
        assertFalse(new Day10(10, 10).isBetween(Point.of(2, 0), Point.of(0, 0), Point.of(1, 0)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(3, 0), Point.of(0, 0), Point.of(1, 0)));
    }

    @Test
    public void notBetweenOneself() {
        assertFalse(new Day10(10, 10).isBetween(Point.of(0, 0), Point.of(0, 0), Point.of(0, 1)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(1, 1), Point.of(1, 1), Point.of(0, 1)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(0, 0), Point.of(0, 0), Point.of(1, 2)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(1, 1), Point.of(0, 0), Point.of(1, 1)));
    }

    @Test
    public void betweenDiagonal() {
        assertFalse(new Day10(20, 20).isBetween(Point.of(2, 2), Point.of(0, 0), Point.of(1, 1)));
        assertFalse(new Day10(20, 20).isBetween(Point.of(3, 3), Point.of(0, 0), Point.of(1, 1)));
        assertFalse(new Day10(20, 20).isBetween(Point.of(4, 4), Point.of(0, 0), Point.of(2, 2)));
        assertFalse(new Day10(20, 20).isBetween(Point.of(9, 9), Point.of(0, 0), Point.of(3, 3)));
        //  F . .
        //  . T .
        //  . . X
        assertFalse(new Day10(3, 3).isBetween(Point.of(2, 2), Point.of(0, 0), Point.of(1, 1)));
        assertFalse(new Day10(5, 5).isBetween(Point.of(4, 3), Point.of(3, 2), Point.of(1, 0)));
        // F . .
        // . X .
        // . . T
        assertTrue(new Day10(10, 10).isBetween(Point.of(1, 1), Point.of(0, 0), Point.of(2, 2)));
        // F . . . .
        // . . x . .
        // . . . . T
        assertTrue(new Day10(10, 10).isBetween(Point.of(2, 2), Point.of(0, 0), Point.of(4, 4)));
        // T . . . .
        // . . x . .
        // . . . . F
        assertTrue(new Day10(3, 3).isBetween(Point.of(2, 2), Point.of(4, 4), Point.of(0, 0)));
        assertFalse(new Day10(5, 5).isBetween(Point.of(4, 3), Point.of(4, 2), Point.of(4, 0)));
    }

    @Test
    public void betweenSameX() {
        assertTrue(new Day10(10, 10).isBetween(Point.of(1, 0), Point.of(0, 0), Point.of(2, 0)));
        assertTrue(new Day10(10, 10).isBetween(Point.of(2, 0), Point.of(0, 0), Point.of(3, 0)));

        // . F X T .
        assertTrue(new Day10(1, 5).isBetween(Point.of(2, 0), Point.of(1, 0), Point.of(3, 0)));
    }

    @Test
    public void betweenSameY() {
        assertTrue(new Day10(10, 10).isBetween(Point.of(0, 1), Point.of(0, 0), Point.of(0, 2)));
        assertTrue(new Day10(10, 10).isBetween(Point.of(0, 2), Point.of(0, 0), Point.of(0, 3)));
    }

    @Test
    public void betweenGiven1() {
        // .T..#
        // .....
        // ##X##
        // ....#
        // ...F#
        assertTrue(new Day10(10, 10).isBetween(Point.of(2, 2), Point.of(3, 4), Point.of(1, 0)));
        assertTrue(new Day10(10, 10).isBetween(Point.of(2, 2), Point.of(1, 0), Point.of(3, 4)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(4, 0), Point.of(3, 4), Point.of(1, 0)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(0, 2), Point.of(3, 4), Point.of(1, 0)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(1, 2), Point.of(3, 4), Point.of(1, 0)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(3, 2), Point.of(3, 4), Point.of(1, 0)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(4, 2), Point.of(3, 4), Point.of(1, 0)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(4, 3), Point.of(3, 4), Point.of(1, 0)));
        assertFalse(new Day10(10, 10).isBetween(Point.of(4, 4), Point.of(3, 4), Point.of(1, 0)));
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
        assertTrue(new Day10(9, 10).isBetween(Point.of(3, 1), Point.of(0, 0), Point.of(6, 2)));
        /*A*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(3, 1), Point.of(0, 0), Point.of(9, 3)));
        /*B*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(3, 2), Point.of(0, 0), Point.of(6, 4)));
        /*B*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(3, 2), Point.of(0, 0), Point.of(9, 6)));
        /*C*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(3, 3), Point.of(0, 0), Point.of(4, 4)));
        /*C*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(3, 3), Point.of(0, 0), Point.of(5, 5)));
        /*C*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(3, 3), Point.of(0, 0), Point.of(6, 6)));
        /*C*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(3, 3), Point.of(0, 0), Point.of(7, 7)));
        /*C*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(3, 3), Point.of(0, 0), Point.of(8, 8)));
        /*E*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(1, 3), Point.of(0, 0), Point.of(2, 6)));
        /*F*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(2, 4), Point.of(0, 0), Point.of(3, 6)));
        /*F*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(2, 4), Point.of(0, 0), Point.of(4, 8)));
        /*F*/
        assertTrue(new Day10(9, 10).isBetween(Point.of(4, 3), Point.of(0, 0), Point.of(8, 6)));
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
        List<List<Integer>> map = Day10.detectMap(input);
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
        List<List<Integer>> map = Day10.laserShootMap(input);
        assertEquals(Point.of(8, 1), Day10.findShot(1, map));
        assertEquals(Point.of(9, 0), Day10.findShot(2, map));
        assertEquals(Point.of(9, 1), Day10.findShot(3, map));
        assertEquals(Point.of(10, 0), Day10.findShot(4, map));
        assertEquals(Point.of(9, 2), Day10.findShot(5, map));
        assertEquals(Point.of(11, 1), Day10.findShot(6, map));
        assertEquals(Point.of(12, 1), Day10.findShot(7, map));
        assertEquals(Point.of(11, 2), Day10.findShot(8, map));
        assertEquals(Point.of(15, 1), Day10.findShot(9, map));

        assertEquals(Point.of(12, 2), Day10.findShot(9 + 1, map));
        assertEquals(Point.of(13, 2), Day10.findShot(9 + 2, map));
        assertEquals(Point.of(14, 2), Day10.findShot(9 + 3, map));
        assertEquals(Point.of(15, 2), Day10.findShot(9 + 4, map));
        assertEquals(Point.of(12, 3), Day10.findShot(9 + 5, map));
        assertEquals(Point.of(16, 4), Day10.findShot(9 + 6, map));
        assertEquals(Point.of(15, 4), Day10.findShot(9 + 7, map));
        assertEquals(Point.of(10, 4), Day10.findShot(9 + 8, map));
        assertEquals(Point.of(4, 4), Day10.findShot(9 + 9, map));
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
        List<List<Integer>> map = Day10.laserShootMap(input);
        assertEquals(Point.of(11, 12), Day10.findShot(1, map));
        assertEquals(Point.of(12, 1), Day10.findShot(2, map));
        assertEquals(Point.of(12, 2), Day10.findShot(3, map));
        assertEquals(Point.of(12, 8), Day10.findShot(10, map));
        assertEquals(Point.of(16, 0), Day10.findShot(20, map));
        assertEquals(Point.of(16, 9), Day10.findShot(50, map));
        assertEquals(Point.of(10, 16), Day10.findShot(100, map));
        assertEquals(Point.of(9, 6), Day10.findShot(199, map));
        assertEquals(Point.of(8, 2), Day10.findShot(200, map));
        assertEquals(Point.of(10, 9), Day10.findShot(201, map));
        assertEquals(Point.of(11, 1), Day10.findShot(299, map));
    }

    @Test
    public void angleOf() {
        Point x = Point.of(2, 2);
        Point a = Point.of(2, 0);
        Point b = Point.of(2, 1);
        Point c = Point.of(2, 4);
        Point d = Point.of(2, 3);
        Point e = Point.of(4, 2);
        Point f = Point.of(0, 2);
        Point g = Point.of(4, 4);
        Point h = Point.of(0, 4);
        Point i = Point.of(0, 0);
        Point j = Point.of(4, 0);
        // i . a . j
        // . . b . .
        // f . x . e
        // . . d . .
        // h . c . g
        assertEquals("", 0.0, Day10.angleOf(x, a), 0.0);
        assertEquals("", 0.0, Day10.angleOf(x, b), 0.0);
        assertEquals(Math.PI, Day10.angleOf(x, c), 0.1);
        assertEquals(Math.PI, Day10.angleOf(x, d), 0.1);
        assertEquals(Math.PI / 2, Day10.angleOf(x, e), 0.1);
        assertEquals(3 * Math.PI / 2, Day10.angleOf(x, f), 0.1);
        assertEquals(3 * Math.PI / 2 + Math.PI / 4, Day10.angleOf(x, i), 0.1);
        assertEquals(Math.PI / 4, Day10.angleOf(x, j), 0.1);

        assertEquals(Math.PI / 2 + Math.PI / 4, Day10.angleOf(x, g), 0.1);
        assertEquals(2 * Math.PI / 2 + Math.PI / 4, Day10.angleOf(x, h), 0.1);
    }

    private void assertBestValue(String input, int value) {
        List<List<Integer>> map = Day10.detectMap(input);
        Point best = Day10.findBest(map);
        assertEquals(Integer.valueOf(value), map.get(best.y).get(best.x));
    }
}
