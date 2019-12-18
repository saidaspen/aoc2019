package se.saidaspen.aoc2019.aoc19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Aoc19 {

    private final Long[] code;

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        Aoc19 app = new Aoc19(input);
        app.part1();
    }

    Aoc19(String input) {
        code = Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
    }

    void part1() {
    }
}
