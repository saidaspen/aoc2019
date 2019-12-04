package se.saidaspen.aoc2019.aoc04;

import java.io.IOException;
import java.util.stream.IntStream;

public class Aoc04 {
    public static void main(String[] args) throws IOException {
        System.out.println(IntStream.range(137683, 596253).filter(Aoc04::meetCriteria).count());
    }

    public static boolean meetCriteria(int val) {
        char[] chars = Integer.toString(val).toCharArray();
        boolean allIncreasing = true;
        for (int i = 0; i < chars.length - 1; i++) {
            allIncreasing &= chars[i] <= chars[i + 1];
        }
        boolean hasTwoAdj = chars[0] == chars[1] && chars[1] != chars[2];
        for (int i = 1; i < chars.length - 2; i++) {
            hasTwoAdj |= chars[i] == chars[i + 1] && chars[i + 1] != chars[i + 2] && chars[i] != chars[i - 1];
        }
        hasTwoAdj |= chars[4] == chars[5] && chars[4] != chars[3];
        return hasTwoAdj && allIncreasing;
    }
}
