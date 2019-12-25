package se.saidaspen.aoc2019;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AocUtil {

    public static String input(int day) throws IOException {
        assert day < 26 && day > 0;
        URL fileResource = ClassLoader.getSystemResource(String.format("aoc%02d.txt", day));
        assert fileResource != null;
        return new String(Files.readAllBytes(Paths.get(fileResource.getPath())));
    }
}
