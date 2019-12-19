package se.saidaspen.aoc2019;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtil {

    public static void printMap(Map<Point, Character> map) {
        System.out.print("\033[2J"); // Clear screen
        int largestX = 0;
        int largestY = 0;
        for (Point p : map.keySet()) {
            largestX = Math.max(p.x, largestX);
            largestY = Math.max(p.y, largestY);
        }
        List<List<Character>> lines = emptyLines(largestX, largestY);
        for (Point p : map.keySet()) {
            lines.get(p.y).set(p.x, map.get(p));
        }
        render(lines);
    }


    private static List<List<Character>> emptyLines(int xMax, int yMax) {
        List<List<Character>> lines = new ArrayList<>();
        for (int row = 0; row <= yMax; row++) {
            List<Character> r = new ArrayList<>();
            for (int col = 0; col <= xMax; col++) {
                r.add(' ');
            }
            lines.add(r);
        }
        return lines;
    }

    private static void render(List<List<Character>> lines) {
        for (List<Character> row : lines) {
            StringBuilder sb = new StringBuilder();
            for (Character c : row) {
                sb.append(c);
            }
            System.out.println(sb);
        }
    }
}
