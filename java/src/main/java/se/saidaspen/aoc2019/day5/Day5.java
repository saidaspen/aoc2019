package se.saidaspen.aoc2019.aoc05;

import se.saidaspen.aoc2019.Day;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Day5 implements Day {

    public static void main(String[] args) throws IOException {
        String fCont = new String(Files.readAllBytes(Paths.get(args[0])));
        Integer[] code = Arrays.stream(fCont.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .boxed()
                .toArray(Integer[]::new);
        run(code);
    }

    private static void run(Integer[] code) {
        int pc = 0;
        do {
            String cmd = String.format("%05d", code[pc]);
            int opCode = Integer.parseInt(cmd.substring(3));
            if (opCode == /*INPUT*/ 3) {
                System.out.println("Input: ");
                int input = Integer.parseInt(new Scanner(System.in).nextLine().trim());
                code[code[pc + 1]] = input;
                pc += 2;
                continue;
            } else if (opCode == /*OUTPUT*/ 4) {
                System.out.println(code[code[pc + 1]]);
                pc += 2;
                continue;
            }
            int param1 = cmd.charAt(2) == '0' ? code[code[pc + 1]] : code[pc + 1];
            int param2 = cmd.charAt(1) == '0' ? code[code[pc + 2]] : code[pc + 2];
            if (opCode == /*ADD*/ 1) {
                code[code[pc + 3]] = param1 + param2;
                pc += 4;
            } else if (opCode == /*MULT*/ 2) {
                code[code[pc + 3]] = param1 * param2;
                pc += 4;
            } else if (opCode == /*JMP_IF_TRUE*/ 5) {
                pc = param1 > 0 ? param2 : pc + 3;
            } else if (opCode == /*JMP_IF_FALSE*/ 6) {
                pc = param1 == 0 ? param2 : pc + 3;
            } else if (opCode == /*LESS_THAN*/ 7) {
                code[code[pc + 3]] = param1 < param2 ? 1 : 0;
                pc += 4;
            } else if (opCode == /* EQUAL*/ 8) {
                code[code[pc + 3]] = param1 == param2 ? 1 : 0;
                pc += 4;
            }
        } while (code[pc] != /* ABORT_CODE*/ 99);
    }

    @Override
    public String part1() {
        return null;
    }

    @Override
    public String part2() {
        return null;
    }
}
