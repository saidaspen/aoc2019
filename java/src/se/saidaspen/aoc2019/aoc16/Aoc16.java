package se.saidaspen.aoc2019.aoc16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Aoc16 {

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        Aoc16 app = new Aoc16(input, 10_000);
        System.out.println(app.part2());
    }

    private static final Integer[] basePattern = new Integer[]{0, 1, 0, -1};
    private final int msgOffset;
    private int[] data;
    private int inSize;

    Aoc16(String input, int repeat) {
        msgOffset = Integer.parseInt(input.substring(0, 7));
        String repeated = input.repeat(repeat);
        inSize = repeated.length();
        data = new int[inSize];
        for (int i = 0; i < inSize; i++) {
            data[i] = repeated.charAt(i) - '0';
        }
    }

    String part1() {
        for (int i = 0; i < data.length; i++) {
            data[i] = applyPattern(i);
        }
        return createString(data);
    }

    String part2() {
        int len = data.length - msgOffset;
        assert msgOffset > inSize / 2;
        for (int i = 0; i < 100; i++) {
            for (int j = len - 2; j >= 0; j--) {
                data[j + msgOffset] = (data[j + msgOffset] + data[j + msgOffset + 1]) % 10;
            }
        }
        return createString(Arrays.copyOfRange(data, msgOffset, msgOffset + 8));
    }

    private int applyPattern(int times) {
        int val = 0;
        int i = times;
        while (i < data.length) {
            int patternPos = ((i + 1) / (times + 1)) % basePattern.length;
            if (patternPos % 2 == 0) {
                i = i + times + 1;
                continue;
            }
            val += data[i] * basePattern[patternPos];
            i++;
        }
        return Math.abs(val) % 10;
    }

    private String createString(int[] out) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : out) {
            sb.append(i);
        }
        return sb.toString();
    }
}
