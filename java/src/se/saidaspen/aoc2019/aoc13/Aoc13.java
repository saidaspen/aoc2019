package se.saidaspen.aoc2019.aoc13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class Aoc13 {


    public static void main(String[] args) throws Exception {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        System.out.println(new Aoc13(input).part1());
    }

    Aoc13(String input) {
        List<String> lines = Arrays.stream(input.split("\n")).map(String::trim).collect(Collectors.toList());
        lines.forEach(System.out::println);
    }

    public String part1() throws Exception {
        return "";
    }
}
