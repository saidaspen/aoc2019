package se.saidaspen.aoc2019.aoc19;

import se.saidaspen.aoc2019.IntComputer;
import se.saidaspen.aoc2019.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Aoc19 {

    public static final int MAX_VAL = 400;
    private final Long[] code;
    private String input;
    private ArrayBlockingQueue<Long> in;
    private ArrayBlockingQueue<Long> out;

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        Aoc19 app = new Aoc19(input);
        app.part2();
    }

    Map<Point, Long> map = new HashMap<>();

    Aoc19(String input) {
        this.input = input;
        code = resetCode();
        in = new ArrayBlockingQueue<>(10000);
        out = new ArrayBlockingQueue<>(10000);
    }

    private Long[] resetCode() {
        Long[] code;
        code = Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        return code;
    }

    void part1() throws InterruptedException {
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10000);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
        IntComputer cpu = new IntComputer(resetCode(), in, out);
        for (int y = 0; y < 50; y++) {
            for (int x = 0; x < 50; x++) {
                if (x == 0 && y == 0) {
                    continue;
                }
                IntComputer cpu1;
                in.put((long) x);
                in.put((long) y);
                cpu1 = new IntComputer(resetCode(), in, out);
                cpu1.run();
                Long tractorVal = out.poll(2, TimeUnit.SECONDS);
                if (tractorVal != null){
                    map.put(Point.of(x, y), tractorVal);
                }
            }
        }
        int cnt = 1;
        for (Point p : map.keySet()) {
            if (map.get(p) > 0) {
                cnt++;
            }
        }
        System.out.println("Affected: " + cnt);
    }


    void part2() throws InterruptedException {
        int x = 0;
        int y = 5;
        while(true) {
            while (!affected(x, y)) {
                x++;
            }
            if (x >= 99 && y >= 99) {
                if (affected(x, y - 99) && affected(x + 99, y - 99)) {
                    System.out.println(String.format("Found it at %s, %s", x, y - 99));
                    System.out.println("Checksum: " + (x * 10000 + (y - 99)));
                    return;
                }
            }
            y++;
        }
    }

    private final Map<String, Boolean> cache = new HashMap<>();

    private boolean affected(int x, int y) throws InterruptedException {
        String key = "" + x + ":" + y;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        IntComputer cpu;
        in.put((long) x);
        in.put((long) y);
        cpu = new IntComputer(resetCode(), in, out);
        cpu.run();
        boolean affected = out.poll(2, TimeUnit.SECONDS) > 0L;
        cache.put(key, affected);
        return affected;
    }

}
