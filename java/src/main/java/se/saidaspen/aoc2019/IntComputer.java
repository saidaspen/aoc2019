package se.saidaspen.aoc2019;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static se.saidaspen.aoc2019.IntComputer.NetworkStatus.IDLE;
import static se.saidaspen.aoc2019.IntComputer.NetworkStatus.RUNNING;
import static se.saidaspen.aoc2019.IntComputer.Status.HALTED;
import static se.saidaspen.aoc2019.IntComputer.Status.WAITING;

public final class IntComputer implements Runnable {

    public enum Status {RUNNING, HALTED, WAITING}

    public enum NetworkStatus {RUNNING, IDLE}

    private final Map<Integer, Long> memory;

    private final BlockingQueue<Long> in, out;
    private final int[] pAddresses = new int[3];
    private int pc = 0;     // Program Counter
    private int timeout = 50;
    private Long rbo = 0L;  // Relative Base Offset
    private Status status = HALTED;
    private NetworkStatus networkStatus = RUNNING;
    private LocalTime idleSince = null;

    public IntComputer(Long[] code, BlockingQueue<Long> in, BlockingQueue<Long> out) {
        memory = new HashMap<>(code.length);
        for (int i = 0; i < code.length; i++) {
            memory.put(i, code[i]);
        }
        this.in = in;
        this.out = out;
    }

    public IntComputer(String codeStr) {
        Long[] code = Arrays.stream(codeStr.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        memory = new HashMap<>(code.length);
        for (int i = 0; i < code.length; i++) {
            memory.put(i, code[i]);
        }
        this.in = new ArrayBlockingQueue<>(10_000);
        this.out = new ArrayBlockingQueue<>(10_000);
    }

    public void run() {
        status = Status.RUNNING;
        try {
            while (load(pc) != /*HALT*/ 99) {
                var cmd = String.format("%05d", load(pc));
                var opCode = Integer.parseInt(cmd.substring(3));
                pAddresses[0] = getParam(cmd, 1); // Param 1
                pAddresses[1] = getParam(cmd, 2); // Param 2
                pAddresses[2] = getParam(cmd, 3); // Param 3
                if (opCode == /*INPUT*/ 3) {
                    status = WAITING;
                    Long val = read();
                    store(pAddresses[0], val);
                    if (val != -1) {
                        status = Status.RUNNING;
                        networkStatus = RUNNING;
                        idleSince = null;
                    } else {
                        if (networkStatus != IDLE) {
                            idleSince = LocalTime.now();
                        }
                        networkStatus = IDLE;
                    }
                    pc += 2;
                } else if (opCode == /*OUTPUT*/ 4) {
                    out.put(load(pAddresses[0]));
                    pc += 2;
                } else if (opCode == /*ADJUST_RBO*/ 9) {
                    rbo += load(pAddresses[0]);
                    pc += 2;
                } else if (opCode == /*JMP_IF_TRUE*/ 5) {
                    pc = load(pAddresses[0]) > 0 ? load(pAddresses[1]).intValue() : pc + 3;
                } else if (opCode == /*JMP_IF_FALSE*/ 6) {
                    pc = load(pAddresses[0]) == 0 ? load(pAddresses[1]).intValue() : pc + 3;
                } else if (opCode == /*ADD*/ 1) {
                    store(pAddresses[2], load(pAddresses[0]) + load(pAddresses[1]));
                    pc += 4;
                } else if (opCode == /*MULT*/ 2) {
                    store(pAddresses[2], load(pAddresses[0]) * load(pAddresses[1]));
                    pc += 4;
                } else if (opCode == /*LESS_THAN*/ 7) {
                    store(pAddresses[2], load(pAddresses[0]) < load(pAddresses[1]) ? 1L : 0L);
                    pc += 4;
                } else if (opCode == /*EQUALS*/ 8) {
                    store(pAddresses[2], load(pAddresses[0]).equals(load(pAddresses[1])) ? 1L : 0L);
                    pc += 4;
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            status = HALTED;
        }
    }

    private Long read() throws InterruptedException {
        Long val = in.poll(timeout, TimeUnit.MILLISECONDS);
        return val == null ? -1L : val;
    }

    private int getParam(String cmd, int i) {
        switch (cmd.charAt(3 - i)) {
            case /*POSITION*/ '0':
                return load(pc + i).intValue();
            case /*IMMEDIATE*/'1':
                return pc + i;
            case /* RELATIVE */ '2':
                return Long.valueOf(load(pc + i) + rbo).intValue();
            default:
                throw new RuntimeException("Unsupported opmode");
        }
    }

    private Long load(int loc) {
        return memory.computeIfAbsent(loc, (x) -> 0L);
    }

    private void store(int loc, Long val) {
        memory.put(loc, val);
    }

    public Status status() {
        return status;
    }

    public void send(long c) throws InterruptedException {
        in.put(c);
    }

    public BlockingQueue<Long> out() {
        return out;
    }

    public BlockingQueue<Long> in() {
        return in;
    }

    public LocalTime idleTime() {
        return this.idleSince;
    }

    public void setTimeout(int i) {
        this.timeout = i;
    }

}
