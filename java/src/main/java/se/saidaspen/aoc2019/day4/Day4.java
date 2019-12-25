package se.saidaspen.aoc2019.aoc04;

import se.saidaspen.aoc2019.Day;

import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public final class Aoc04 implements Day {

    private final String input;

    public Aoc04(String input) {
        this.input = input;
    }

    public static boolean meetsCriteria(int val, boolean allowPartOfLarger) {
        char[] chars = Integer.toString(val).toCharArray();
        boolean allIncreasing = true;
        for (int i = 0; i < chars.length - 1; i++) {
            allIncreasing &= chars[i] <= chars[i + 1];
        }
        Map<Character, Long> occurrences = Integer.toString(val).chars()
                .mapToObj(c -> (char) c).collect(groupingBy(c -> c, counting()));
        boolean hasAdj = occurrences.entrySet().stream().anyMatch(entry -> entry.getValue() > 1L);
        return allIncreasing && hasAdj && (allowPartOfLarger || occurrences.containsValue(2L));
    }

    public String part1() {
        return Long.toString(toStream(input).filter(i -> meetsCriteria(i, true)).count());
    }

    private IntStream toStream(String input) {
        String[] split = input.split("-");
        int start = parseInt(split[0].trim());
        int end = split.length > 1 ? parseInt(split[1].trim()) : start;
        return IntStream.range(start, end);
    }

    public String part2() {
        return Long.toString(toStream(input).filter(i -> meetsCriteria(i, false)).count());
    }
}
