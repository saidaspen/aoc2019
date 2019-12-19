package se.saidaspen.aoc2019.aoc19;

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import se.saidaspen.aoc2019.IntComputer;

public final class Aoc19 {

    private final ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(1000);
    private final ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(1000);
    private final Long[] code;
    private final Map<String, Boolean> affectedCache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Aoc19 app = new Aoc19(new String(Files.readAllBytes(Paths.get(args[0]))));
        app.part1();
        app.part2();
    }

    private Aoc19(String input) {
        code = stream(input.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
    }

    @SuppressWarnings("unused")
    private void part1() {
        int numAffected = range(0, 50).flatMap(y -> range(0, 50).map(x -> affected(x, y) ? 1 : 0)).sum();
        System.out.println("Affected coordinates: " + numAffected);
    }

    private void part2() {
        int x = 0, y = 5;
        while (true) {
            while (!affected(x, y))
                x++;
            if (x >= 99 && y >= 99) {
                if (affected(x, y - 99) && affected(x + 99, y - 99)) {
                    System.out.println(String.format("Pos: %s, %s. Checksum: %s", x, y - 99, (x * 10000 + (y - 99))));
                    return;
                }
            }
            y++;
        }
    }

    private boolean affected(int x, int y) {
        String key = "" + x + ":" + y;
        if (!affectedCache.containsKey(key)) {
            put(x, y);
            new IntComputer(Arrays.copyOf(code, code.length), in, out).run();
            affectedCache.put(key, poll() > 0);
        }
        return affectedCache.get(key);
    }

    private Long poll() {
        try {
            return out.poll(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void put(long... x) {
        try {
            for (long val : x) {
                in.put(val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
