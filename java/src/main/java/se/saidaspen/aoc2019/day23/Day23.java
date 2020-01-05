package se.saidaspen.aoc2019.day23;

import lombok.Value;
import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.IntComputer;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * Solution to Advent of Code 2019 Day 23
 * The original puzzle can be found here: https://adventofcode.com/2019/day/23
 * */
public final class Day23 implements Day {

    private final String code;

    private static abstract class Control {
        Long val;
        abstract void accept(Long val);
        boolean shouldContinue() {return val == null;}
    }

    Day23(String input) { this.code = input; }

    private void run(Control spy) {
        try {
            // Setup computers
            IntComputer[] cpus = new IntComputer[50];
            for (int i = 0; i < 50; i++) {
                cpus[i] = new IntComputer(code);
                cpus[i].in().put((long) i); // Set network address
            }
            // Setup Network devices
            NAT nat = new NAT(cpus, spy);
            NetworkSwitch networkSwitch = new NetworkSwitch(cpus, nat);
            // Start Network, each device runs in its own thread.
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(cpus.length + 2);
            pool.execute(networkSwitch);
            pool.execute(nat);
            for (int i = 0; i < 50; i++) {
                pool.execute(cpus[i]);
            }
            pool.shutdown();
            // Wait until devices threads are finished, or timeout.
            pool.awaitTermination(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpectedly interrupted.", e);
        }
    }

    @Override public String part1() {
        Control spy = new Control() {public void accept(Long l) { if (val == null) val = l; }};
        run(spy);
        return spy.val.toString();
    }

    @Override public String part2() {
        Control spy = new Control() {
            Long lastY = null;
            public void accept(Long l) {
                if (l.equals(lastY) && val == null) val = l;
                lastY = l;
            }
        };
        run(spy);
        return spy.val.toString();
    }

    /**
     * Switches a network of computers.
     * Reads everything computers send out, and routes it to the address specified.
     * Address 255 is reserved for the Network NAT (Not Always Transmitting).
     */
    @Value private static class NetworkSwitch implements Runnable {
        private final IntComputer[] computers;
        private final NAT nat;

        @Override
        public void run() {
            while (true) {
                for (IntComputer computer : computers) {
                    Long addr = computer.out().poll();
                    if (addr != null) {
                        try {
                            Long x = computer.out().poll(20, TimeUnit.MILLISECONDS);
                            Long y = computer.out().poll(20, TimeUnit.MILLISECONDS);
                            if (x == null || y == null) {
                                System.err.println("Timed out waiting for data.");
                                return;
                            }
                            if (addr == 255) {
                                nat.accept(x, y);
                            } else {
                                computers[Math.toIntExact(addr)].in().put(x);
                                computers[Math.toIntExact(addr)].in().put(y);
                            }
                        } catch (Exception e) {
                            System.err.println("Error while processing data. Caused by " + e.getMessage());
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * NAT (Not Always Transmitting).
     * Keeps track of packages to be sent out when network is idle. When the network is considered idle,
     * no packages have been received by any computer on the network after a certain time frame, sends
     * the last package received to network address 0;
     */
    private static class NAT implements Runnable {
        public static final long IDLE_LIMIT_MS = 100L;
        private final IntComputer[] cpus;
        private Control spy;

        // Make sure NAT package values are not changed while being used.
        private final ReentrantLock valLock = new ReentrantLock();
        private Long x;
        private Long y;

        public NAT(IntComputer[] cpus, Control spy) {
            this.cpus = cpus;
            this.spy = spy;
        }

        public void accept(Long x, Long y) {
            valLock.lock();
            this.x = x;
            this.y = y;
            valLock.unlock();
        }

        @Override
        public void run() {
            while (true) {
                if (isNetworkIdle() && x != null && y != null) {
                    try {
                        spy.accept(y);
                        if (!spy.shouldContinue()) { return; }
                        valLock.lock();
                        cpus[0].in().put(x);
                        cpus[0].in().put(y);
                        x = null;
                        y = null;
                        valLock.unlock();
                    } catch (Exception e) {
                        System.err.println("Error while processing NAT " + e.getMessage());
                        return;
                    }
                }
            }
        }

        private boolean isNetworkIdle() {
            LocalTime now = LocalTime.now();
            for (IntComputer intComputer : cpus) {
                LocalTime idleTime = intComputer.idleTime();
                if (idleTime == null || MILLIS.between(idleTime, now) < IDLE_LIMIT_MS) {
                    return false;
                }
            }
            return true;
        }
    }
}
