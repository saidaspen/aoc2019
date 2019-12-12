package se.saidaspen.aoc2019.aoc12;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public final class Aoc12 {

    private final List<Moon> moons;
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;
    private static final int[] DIRS = new int[]{X, Y, Z};
    private static final long[] PRIMES = {50331653, 1610612741, 805306457, 3145739, 100663319, 201326611, 402653189, 9432859832898L};

    public Aoc12(String input) {
        moons = Arrays.stream(input.split("\n"))
                .map(l -> l.replaceAll("[<>=xyz ]", ""))
                .map(l -> l.split(","))
                .map(a -> new Moon(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2])))
                .collect(Collectors.toList());
    }

    public long getSystemEnergy(int steps) {
        for (long step = 0; step < steps; step++) {
            stepSystem();
        }
        return moons.stream().mapToLong(Moon::energy).sum();
    }

    public long getSystemPeriod() {
        int step = 0;
        TreeSet<Long>[] states = new TreeSet[]{new TreeSet<Long>(), new TreeSet<Long>(), new TreeSet<Long>()};
        long[] periods = new long[]{0, 0, 0};
        while (periods[X] == 0 || periods[Y] == 0 || periods[Z] == 0) {
            for (int d = 0; d < DIRS.length; d++) {
                if (periods[d] != 0)
                    continue;
                long state = systemState(d);
                if (states[d].contains(state)) {
                    periods[d] = step;
                } else {
                    states[d].add(state);
                }
            }
            stepSystem();
            step++;
        }
        return lcm(lcm(periods[X], periods[Y]), periods[Z]);
    }

    private long systemState(int d) {
        return PRIMES[(moons.size()) % PRIMES.length] * moons.get(0).coords[d] +
                PRIMES[(moons.size() + 1) % PRIMES.length] * moons.get(1).coords[d] +
                PRIMES[(moons.size() + 2) % PRIMES.length] * moons.get(2).coords[d] +
                PRIMES[(moons.size() + 3) % PRIMES.length] * moons.get(3).coords[d] +
                PRIMES[(moons.size() + 4) % PRIMES.length] * moons.get(0).velocities[d] +
                PRIMES[(moons.size() + 5) % PRIMES.length] * moons.get(1).velocities[d] +
                PRIMES[(moons.size() + 6) % PRIMES.length] * moons.get(2).velocities[d] +
                PRIMES[(moons.size() + 7) % PRIMES.length] * moons.get(3).velocities[d];
    }

    private void stepSystem() {
        for (int i = 0; i < moons.size(); i++) {
            Moon moonA = moons.get(i);
            for (int j = i + 1; j < moons.size(); j++) {
                Moon moonB = moons.get(j);
                for (int d = 0; d < DIRS.length; d++) {
                    if (moonA.coords[d] < moonB.coords[d]) {
                        moonA.velocities[d]++;
                        moonB.velocities[d]--;
                    } else if (moonA.coords[d] > moonB.coords[d]) {
                        moonA.velocities[d]--;
                        moonB.velocities[d]++;
                    }
                }
            }
            moonA.move();
        }
    }

    public static long gcd(long number1, long number2) {
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

    public static long lcm(long number1, long number2) {
        return (number1 == 0 || number2 == 0) ? 0 : Math.abs(number1 * number2) / gcd(number1, number2);
    }

    private static final class Moon implements Cloneable {
        int[] coords = new int[]{0, 0, 0};
        int[] velocities = new int[]{0, 0, 0};

        public Moon(int x, int y, int z) {
            coords[X] = x;
            coords[Y] = y;
            coords[Z] = z;
        }

        public long energy() {
            return (Math.abs(velocities[X]) + Math.abs(velocities[Y]) + Math.abs(velocities[Z])) * (Math.abs(coords[X]) + Math.abs(coords[Y]) + Math.abs(coords[Z]));
        }

        public void move() {
            for (int d = 0; d < DIRS.length; d++) {
                coords[d] += velocities[d];
            }
        }
    }
}
