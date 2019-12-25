package se.saidaspen.aoc2019.aoc23;

import se.saidaspen.aoc2019.IntComputer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static java.time.temporal.ChronoUnit.MILLIS;

public final class Aoc23 {

    private String code;

    public static void main(String[] args) throws IOException, InterruptedException {
        Aoc23 app = new Aoc23(new String(Files.readAllBytes(Paths.get(args[0]))));
        app.run();
    }

    private Aoc23(String input) {
        this.code = input;
    }

    @SuppressWarnings("unused")
    private void run() throws InterruptedException {
        // Setup computers
        IntComputer[] cpus = new IntComputer[50];
        for (int i = 0; i < 50; i++) {
            cpus[i] = new IntComputer(code);
            cpus[i].in().put((long) i); // Set network address
        }

        // Setup Network devices
        NAT nat = new NAT(cpus);
        NetworkSwitch networkSwitch = new NetworkSwitch(cpus, nat);

        // Start Network.
        // Each device runs in its own thread.
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(cpus.length + 2);
        pool.execute(networkSwitch);
        pool.execute(nat);
        for (int i = 0; i < 50; i++) {
            pool.execute(cpus[i]);
        }
        pool.shutdown();

        // Wait until devices threads are finished, or timeout.
        pool.awaitTermination(10L, TimeUnit.SECONDS);
    }

    /**
     * Switches a network of computers.
     * Reads everything computers send out, and routes it to the address specified.
     * Address 255 is reserved for the Network NAT (Not Always Transmitting).
     */
    private static class NetworkSwitch implements Runnable {
        private final IntComputer[] computers;
        private final NAT nat;

        public NetworkSwitch(IntComputer[] computers, NAT nat) {
            this.computers = computers;
            this.nat = nat;
        }

        @Override
        public void run() {
            while (true) {
                for (IntComputer computer : computers) {
                    Long addr = computer.out().poll();
                    if (addr != null) {
                        try {
                            Long x = computer.out().poll(500, TimeUnit.MILLISECONDS);
                            Long y = computer.out().poll(500, TimeUnit.MILLISECONDS);
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
        public static final long IDLE_LIMIT_MS = 300L;
        private final IntComputer[] cpus;

        // Make sure NAT package values are not changed while being used.
        private final ReentrantLock valLock = new ReentrantLock();
        private Long lastSentY;
        private Long x;
        private Long y;

        public NAT(IntComputer[] cpus) {
            this.cpus = cpus;
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
                        if (lastSentY != null && lastSentY.equals(y)) {
                            System.out.println("Sent Y value twice: " + y);
                        }
                        valLock.lock();
                        cpus[0].in().put(x);
                        cpus[0].in().put(y);
                        lastSentY = y;
                        x = null;
                        y = null;
                        valLock.unlock();
                    } catch (Exception e) {
                        System.err.println("Error while processing NAT" + e.getMessage());
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
