package se.saidaspen.aoc2019.day5;

import se.saidaspen.aoc2019.Day;

import java.util.Stack;

import static se.saidaspen.aoc2019.AocUtil.toCode;

/**
 * Solution for Advent of Code 2019 Day 5
 * The original puzzle can be found here: https://adventofcode.com/2019/day/5
 */
public final class Day5 implements Day {

    private final String input;

    public Day5(String input) {
        this.input = input;
    }

    @Override
    public String part1() {
        return Integer.toString(run(toCode(input), 1).pop());
    }

    @Override
    public String part2() {
        return Integer.toString(run(toCode(input), 5).pop());
    }

    private static Stack<Integer> run(Integer[] code, int in) {
        int pc = 0;
        Stack<Integer> out = new Stack<>();
        do {
            String cmd = String.format("%05d", code[pc]);
            int opCode = Integer.parseInt(cmd.substring(3));
            if (opCode == /*INPUT*/ 3) {
                code[code[pc + 1]] = in;
                pc += 2;
                continue;
            } else if (opCode == /*OUTPUT*/ 4) {
                out.add(code[code[pc + 1]]);
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
        return out;
    }
}
