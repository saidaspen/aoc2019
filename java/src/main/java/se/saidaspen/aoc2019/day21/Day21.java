package se.saidaspen.aoc2019.day21;

import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.IntComputer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution to Advent of Code 2019 Day 21
 * The original puzzle can be found here: https://adventofcode.com/2019/day/21
 * */
public final class Day21 implements Day {

    private final String input;
    private final Pattern digitsPattern;

    Day21(String input) {
        this.input = input;
        digitsPattern = Pattern.compile("(\\d+)");
    }

    public String part1() {
        int tiles = 4;
        List<Integer> possibleSolutions = new LinkedList<>();
        possibleSolutions.add(0b1110);
        List<Integer> holes = new ArrayList<>();
        while (!possibleSolutions.isEmpty()) {
            Integer attempt = possibleSolutions.remove(0);
            String commands = toCommand(attempt, tiles);
            commands = commands + "WALK\n";
            String output = run(commands);
            System.out.println(Integer.toBinaryString(attempt) + " " + commands.replaceAll("\n", ", ") + " -> " + output);
            Matcher match = digitsPattern.matcher(output);
            if (match.find()) {
                System.out.println("Finished with command " + commands);
                return match.group(1);
            } else {
                output = output.replaceAll("@", ".");
                int firstHole = output.indexOf('.');
                String hole = output.substring(firstHole, firstHole + tiles).replaceAll("@", ".").replaceAll("\\.", "0").replaceAll("#", "1");
                holes.add(Integer.parseInt(hole, 2));
                //solution = toSolution(holes, tiles);
                System.out.println("HOLES: " + Arrays.toString(holes.toArray()));
            }
        }
        return "";
    }

    private Object toSolution(List<Integer> holes, int tiles) {
        int[] bitMap = new int[tiles];
        for (Integer hole : holes) {
            for (int i = 0; i < tiles; i++) {

            }
        }
        return null;
    }

    private String toCommand(int cmd, int length) {
        List<String> negCommands = new ArrayList<>();
        List<String> posCommands = new ArrayList<>();
        for (int i = length; i > 0; i--) {
            String pos = String.valueOf((char) ('A' + (length - i)));
            if ((cmd & (1 << i)) > 0) {
                posCommands.add(pos);
            } else {
                negCommands.add(pos);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < negCommands.size(); i++) {
            if (i == 0) {
                sb.append(String.format("NOT %s J\n", negCommands.get(i)));
            } else {
                sb.append(String.format("NOT %s T\n", negCommands.get(i)));
                sb.append("AND T J\n");
            }
        }
        for (String posCommand : posCommands) {
            sb.append(String.format("AND %s J\n", posCommand));
        }
        return sb.toString();
    }

    public String part2() {
        int tiles = 9;
        for (int i = ((int) Math.pow(2, tiles) - 2); i > 0; i--) {
            String commands = toCommand(i, tiles);
            commands = commands + "RUN\n";
            String output = run(commands);
            //System.out.println(commands.replaceAll("\n", "-") + " -> " + output);
            Matcher match = digitsPattern.matcher(output);
            if (match.find()) {
                System.out.println("Finished with command " + commands);
                return match.group(1);
            }
        }
        return "";
        /*return run("NOT A J\n" +
                "NOT B T\n" +
                "OR T J\n" +
                "NOT C T\n" +
                "OR T J\n" +
                "AND D J\n" +
                "NOT E T\n" +
                "NOT T T\n" +
                "OR H T\n" +
                "AND T J\n" +
                "RUN\n");*/
    }

    private String run(String program) {
        try {
            IntComputer cpu = new IntComputer(input);
            send(cpu, program);
            new Thread(cpu).start();
            waitFor(cpu);
            String output = print(cpu.out());
            System.out.println(output);
            String[] lines = output.split("\n");
            return lines[lines.length - 1];
        } catch (Exception e) {
            throw new RuntimeException("Exception during simulation.", e);
        }
    }

    private void send(IntComputer cpu, String program) throws InterruptedException {
        for (char c : program.toCharArray()) {
            cpu.send((int) c);
        }
    }

    private String print(BlockingQueue<Long> out) throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        Long outVal = out.poll(5, TimeUnit.SECONDS);
        while (outVal != null) {
            char c = (char) outVal.byteValue();
            if (isMapChar(c) || isText(c))
                sb.append(c);
            else {
                sb.append(outVal);
            }
            outVal = out.poll(2, TimeUnit.SECONDS);
        }
        return sb.toString();
    }

    private void waitFor(IntComputer cpu) throws InterruptedException {
        do {
            Thread.sleep(100L);
        } while (cpu.status() == IntComputer.Status.RUNNING);
    }

    private boolean isText(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ' || c == ':' || c == '\'' || c == '\n';
    }

    private boolean isMapChar(char c) {
        return c == '.' || c == '#' || c == '@';
    }
}
