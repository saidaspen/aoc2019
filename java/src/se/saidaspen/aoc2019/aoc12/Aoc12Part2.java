package se.saidaspen.aoc2019.aoc12;

import java.io.IOException;
import java.util.*;

public class Aoc12 {


    public static void main(String[] args) throws IOException, InterruptedException {
        List<Moon> moons = new ArrayList<>();
        /*Moon p1 = new Moon("A", 17, -9, 4);
        Moon p2 = new Moon("B", 2, 2, -13);
        Moon p3 = new Moon("C", -1, 5, -1);
        Moon p4 = new Moon("D", 4, 7, -7);
         */

        /*Moon p1 = new Moon("A", -1,0,2);
        Moon p2 = new Moon("B",2,-10,-7);
        Moon p3 = new Moon("C",4,-8,8);
        Moon p4 = new Moon("D",3,5,-1);
         */

        Moon p1 = new Moon("A", -8, -10, -0);
        Moon p2 = new Moon("B", 5, 5, 10);
        Moon p3 = new Moon("C", 2, -7, 3);
        Moon p4 = new Moon("D", 9, 8, -3);

        moons.add(p1);
        moons.add(p2);
        moons.add(p3);
        moons.add(p4);

        List<String> logX = new ArrayList<>();
        List<String> logY = new ArrayList<>();
        List<String> logZ = new ArrayList<>();

        long periodX =0;
        long periodY =0;
        long periodZ =0;

        for (long step = 0; step < 1000; step++) {
            if (step % 100_000 == 0) {
                System.out.println(step);
            }
            String stateX = String.format("%s-%s-%s-%s %s-%s-%s-%s", moons.get(0).x, moons.get(1).x, moons.get(2).x, moons.get(3).x, moons.get(0).vx, moons.get(1).vx, moons.get(2).vx, moons.get(3).vx);
            String stateY = String.format("%s-%s-%s-%s %s-%s-%s-%s", moons.get(0).y, moons.get(1).y, moons.get(2).y, moons.get(3).y, moons.get(0).vy, moons.get(1).vy, moons.get(2).vy, moons.get(3).vy);
            String stateZ = String.format("%s-%s-%s-%s %s-%s-%s-%s", moons.get(0).z, moons.get(1).z, moons.get(2).z, moons.get(3).z, moons.get(0).vz, moons.get(1).vz, moons.get(2).vz, moons.get(3).vz);
            if (periodX == 0) {
                if (logX.contains(stateX)) {
                    periodX = step;
                } else {
                    logX.add(stateX);
                }
            }
            if (periodY == 0) {
                if (logY.contains(stateY)) {
                    periodY = step;
                } else {
                    logY.add(stateY);
                }
            }
            if (periodZ == 0) {
                if (logZ.contains(stateZ)) {
                    periodZ = step;
                } else {
                    logZ.add(stateZ);
                }
            }
            if (periodX != 0 && periodY != 0 && periodZ != 0) {
                break;
            }
            /*System.out.println("After Step " + step);
            for (Moon m : moons) {
                System.out.println(m);
            }
            System.out.println();

             */

            for (int i = 0; i < moons.size(); i++) {
                Moon moonA = moons.get(i);
                // Update velocities
                for (int j = i + 1; j < moons.size(); j++) {
                    Moon moonB = moons.get(j);
                    if (moonA.x < moonB.x) {
                        moonA.vx++;
                        moonB.vx--;
                    } else if (moonA.x > moonB.x) {
                        moonA.vx--;
                        moonB.vx++;
                    }
                    if (moonA.y < moonB.y) {
                        moonA.vy++;
                        moonB.vy--;
                    } else if (moonA.y > moonB.y) {
                        moonA.vy--;
                        moonB.vy++;
                    }
                    if (moonA.z < moonB.z) {
                        moonA.vz++;
                        moonB.vz--;
                    } else if (moonA.z > moonB.z) {
                        moonA.vz--;
                        moonB.vz++;
                    }
                }
                moonA.x += moonA.vx;
                moonA.y += moonA.vy;
                moonA.z += moonA.vz;
            }
        }

        long totEnergy = 0;
        // Apply velocity
        for (Moon m : moons) {
            System.out.println(String.format("Kin: %s, Pot: %s, Tot so far: %s", m.kin(), m.pot(), totEnergy));
            totEnergy = totEnergy + (m.kin() * m.pot());
        }

        System.out.println(String.format("periodx: %s, periody: %s, periodz:%s", periodX, periodY, periodZ));
        System.out.println(String.format("Universe repeats every %s steps", lcd(lcd(periodX, periodY), periodZ)));
        System.out.println(totEnergy);
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

    public static long lcd(long number1, long number2) {
        if (number1 == 0 || number2 == 0)
            return 0;
        else {
            long gcd = gcd(number1, number2);
            return Math.abs(number1 * number2) / gcd;
        }
    }

    private static class Moon {
        public String name;
        int x;
        int y;
        int z = 0;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Moon moon = (Moon) o;
            return x == moon.x &&
                    y == moon.y &&
                    z == moon.z &&
                    vx == moon.vx &&
                    vy == moon.vy &&
                    vz == moon.vz &&
                    Objects.equals(name, moon.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, x, y, z, vx, vy, vz);
        }

        int vx, vy, vz = 0;

        public Moon(String name, int x, int y, int z) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public long pot() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }

        public long kin() {
            return Math.abs(vx) + Math.abs(vy) + Math.abs(vz);
        }

        public String toString() {
            return String.format("pos=<x=%s, y=%s, z=%s>, vel=<x=%s, y=%s, z=%s>,", x, y, z, vx, vy, vz);
        }

        public Moon createClone() {
            Moon m = new Moon(this.name, this.x, this.y, this.z);
            m.vx = this.vx;
            m.vy = this.vy;
            m.vz = this.vz;
            return m;
        }
    }

    private static class State {
        private Map<String, Moon> moons = new HashMap<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            for (String mName : moons.keySet())
                if (!this.moons.get(mName).equals(state.moons.get(mName))) {
                    return false;
                }
            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(moons);
        }
    }
}
