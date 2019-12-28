package se.saidaspen.aoc2019;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.lang.String.format;

public class AocUtil {

    public static String input(int day) throws IOException {
        assert day < 26 && day > 0;
        URL fileResource = ClassLoader.getSystemResource(format("aoc%02d.txt", day));
        assert fileResource != null;
        return new String(Files.readAllBytes(Paths.get(fileResource.getPath())));
    }

    public static Integer[] toCode(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
    }

    public static Long[] toLongCode(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong).boxed().toArray(Long[]::new);
    }
}
