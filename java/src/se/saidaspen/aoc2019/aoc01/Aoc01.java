package se.saidaspen.aoc2019.aoc01;

import java.io.IOException;
import java.nio.file.Paths;

import static java.lang.Math.floor;
import static java.nio.file.Files.lines;

public class Aoc01 {

    public static void main(String[] args) throws IOException {
        System.out.println(lines(Paths.get(args[0]))
                .mapToDouble(Double::parseDouble)
                .map(Aoc01::fuelFor)
                .sum());
    }

    private static double fuelFor(double d) {
        double fuelReq = floor(d / 3) - 2;
        return fuelReq < 1.0 ? 0.0 : fuelReq + fuelFor(fuelReq);
    }
}
