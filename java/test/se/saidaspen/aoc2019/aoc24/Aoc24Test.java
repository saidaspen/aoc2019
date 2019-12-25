package se.saidaspen.aoc2019.aoc24;

import org.junit.Test;
import se.saidaspen.aoc2019.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Aoc24Test {


    @Test
    public void part1Example1() throws Exception {
        String input =
                ".....\n" +
                ".....\n" +
                ".....\n" +
                "#....\n" +
                ".#...";
        List<String> lines = Arrays.stream(input.split("\n")).collect(Collectors.toList());
        Map<Point, Character> map = new HashMap<>();

        for (int i = 0; i < lines.size(); i++) {
            char[] line = lines.get(i).toCharArray();
            for (int col = 0; col < line.length; col++) {
                Point p = Point.of(col, i);
                map.put(p, line[col]);
            }
        }

        assertEquals(2129920, Aoc24.bioDiversity(map));
    }

    //A bug dies (becoming an empty space) unless there is exactly one bug adjacent to it.
    //An empty space becomes infested with a bug if exactly one or two bugs are adjacent to it.

    //that is, within the same minute, the number of adjacent bugs is counted for every tile first, and then the tiles are updated.
}
