package se.saidaspen.aoc2019.day12;


import se.saidaspen.aoc2019.Day;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Solution to Advent of Code 2019 Day 12
 * The original puzzle can be found here: https://adventofcode.com/2019/day/12
 */
final class Day12 implements Day {

    private final List<Moon> moons;
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    public static void main(String[] args) throws IOException {
        System.out.println(new Day12(new String(Files.readAllBytes(Paths.get(args[0])))).getSystemPeriod());
    }

    Day12(String input) {
        moons = Arrays.stream(input.split("\n"))
                .map(l -> l.replaceAll("[<>=xyz ]", ""))
                .map(l -> l.split(","))
                .map(a -> new Moon(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2])))
                .collect(Collectors.toList());
    }

    long getSystemEnergy(int steps) {
        for (long step = 0; step < steps; step++) {
            stepSystem();
        }
        return moons.stream().mapToLong(Moon::energy).sum();
    }

    long getSystemPeriod() {
        int step = 0;
        long[] periods = new long[]{0, 0, 0};
        while (periods[X] == 0 || periods[Y] == 0 || periods[Z] == 0) {
            for (int d = 0; d < 3; d++) {
                boolean sameAsInit = true;
                for (Moon m : moons) {
                    sameAsInit &= m.velocities[d] == 0;
                }
                if (periods[d] == 0 && sameAsInit) {
                    periods[d] = 2 * step;
                }
            }
            stepSystem();
            step++;
        }
        return lcm(lcm(periods[X], periods[Y]), periods[Z]);
    }

    private void stepSystem() {
        for (int i = 0; i < moons.size(); i++) {
            Moon moonA = moons.get(i);
            for (int j = i + 1; j < moons.size(); j++) {
                Moon moonB = moons.get(j);
                for (int d = 0; d < 3; d++) {
                    if (moonA.pos[d] < moonB.pos[d]) {
                        moonA.velocities[d]++;
                        moonB.velocities[d]--;
                    } else if (moonA.pos[d] > moonB.pos[d]) {
                        moonA.velocities[d]--;
                        moonB.velocities[d]++;
                    }
                }
            }
            moonA.move();
        }
    }

    private static long gcd(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return number1 + number2;
        } else {
            long absNumber1 = Math.abs(number1);
            long absNumber2 = Math.abs(number2);
            long biggerValue = Math.max(absNumber1, absNumber2);
            long smallerValue = Math.min(absNumber1, absNumber2);
            return gcd(biggerValue % smallerValue, smallerValue);
        }
    }

    private static long lcm(long number1, long number2) {
        return (number1 == 0 || number2 == 0) ? 0 : Math.abs(number1 * number2) / gcd(number1, number2);
    }

    @Override
    public String part1() {
        return Long.toString(getSystemEnergy(1000));
    }

    @Override
    public String part2() {
        return Long.toString(getSystemPeriod());
    }

    private static final class Moon implements Cloneable {
        int[] pos = new int[]{0, 0, 0};
        int[] velocities = new int[]{0, 0, 0};

        Moon(int x, int y, int z) {
            pos[X] = x;
            pos[Y] = y;
            pos[Z] = z;
        }

        long energy() {
            return (Math.abs(velocities[X]) + Math.abs(velocities[Y]) + Math.abs(velocities[Z])) * (Math.abs(pos[X]) + Math.abs(pos[Y]) + Math.abs(pos[Z]));
        }

        void move() {
            for (int d = 0; d < 3; d++) {
                pos[d] += velocities[d];
            }
        }
    }
}
