package se.saidaspen.aoc2019.aoc09;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public class IntComputer implements Runnable {
    private final Map<Integer, Long> memory;
    private final BlockingQueue<Long> in;
    private final BlockingQueue<Long> out;
    private boolean debugLog = false;
    private int pc = 0;
    private Long relativeBaseOffset = 0L;

    public IntComputer(Long[] code, BlockingQueue<Long> in, BlockingQueue<Long> out) {
        memory = new HashMap<>(code.length);
        for (int i = 0; i < code.length; i++) {
            memory.put(i, code[i]);
        }
        this.in = in;
        this.out = out;
    }

    private enum ParamMode {
        POSITION(0),
        IMMEDIATE(1),
        RELATIVE(2);
        int val;

        ParamMode(int i) {
            this.val = i;
        }
    }

    private enum Command {
        ADD(1),
        MULT(2),
        INPUT(3),
        OUTPUT(4),
        JMP_IF_TRUE(5),
        JMP_IF_FALSE(6),
        LESS_THAN(7),
        EQUAL(9),
        ADJUST_RBO(9),
        HALT(99);
        private final int val;

        Command(int i) {
            this.val = i;
        }

        public static Command valueOf(int val) {
            switch (val) {
                case 1:
                    return ADD;
                case 2:
                    return MULT;
                case 3:
                    return INPUT;
                case 4:
                    return OUTPUT;
                case 5:
                    return JMP_IF_TRUE;
                case 6:
                    return JMP_IF_FALSE;
                case 7:
                    return LESS_THAN;
                case 8:
                    return EQUAL;
                case 9:
                    return ADJUST_RBO;
                case 99:
                    return HALT;
                default:
                    throw new RuntimeException("Unsupported command with code: " + val);
            }
        }
    }

    private static class Parameter {
        private final ParamMode mode;
        private final int addr;
        private final Map<Integer, Long> memory;

        Parameter(ParamMode mode, int addr, Map<Integer, Long> memory) {
            this.mode = mode;
            this.addr = addr;
            this.memory = memory;
        }

        @Override
        public String toString() {
            return "Parameter{mode=" + mode + ", addr=" + addr + '}';
        }

        public Long get() {
            return memory.computeIfAbsent(addr, (x) -> 0L);
        }
    }

    public void run() {
        try {
            do {
                String cmd = String.format("%05d", load(pc));
                Command opCode = Command.valueOf(Integer.parseInt(cmd.substring(3)));
                Parameter param1 = getParam(cmd, 1);
                Parameter param2 = getParam(cmd, 2);
                Parameter param3 = getParam(cmd, 3);
                printCommand(cmd, param1, param2, param3, relativeBaseOffset, opCode);
                if (opCode == Command.INPUT) {
                    store(param1.addr, in.poll(10L, TimeUnit.DAYS));
                    pc += 2;
                } else if (opCode == Command.OUTPUT) {
                    out.put(param1.get());
                    pc += 2;
                } else if (opCode == Command.ADJUST_RBO) {
                    relativeBaseOffset = relativeBaseOffset + param1.get();
                    pc += 2;
                } else if (opCode == Command.JMP_IF_TRUE) {
                    pc = (param1.get() > 0) ? param2.get().intValue() : pc + 3;
                } else if (opCode == Command.JMP_IF_FALSE) {
                    pc = param1.get() == 0 ? param2.get().intValue() : pc + 3;
                } else if (opCode == Command.ADD) {
                    store(param3.addr, param1.get() + param2.get());
                    pc += 4;
                } else if (opCode == Command.MULT) {
                    store(param3.addr, param1.get() * param2.get());
                    pc += 4;
                } else if (opCode == Command.LESS_THAN) {
                    store(param3.addr, param1.get() < param2.get() ? 1L : 0L);
                    pc += 4;
                } else if (opCode == Command.EQUAL) {
                    store(param3.addr, param1.get().equals(param2.get()) ? 1L : 0L);
                    pc += 4;
                }
            } while (load(pc) != Command.HALT.val);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private Parameter getParam(String cmd, int i) {
        char opMode = cmd.charAt(3 - i);
        if (opMode == '0') {
            return new Parameter(ParamMode.POSITION, load(pc + i).intValue(), memory);
        } else if (opMode == '1') {
            return new Parameter(ParamMode.IMMEDIATE, pc + i, memory);
        } else {
            int addr = Long.valueOf(load(pc + i) + relativeBaseOffset).intValue();
            return new Parameter(ParamMode.RELATIVE, addr, memory);
        }
    }

    private Long load(int loc) {
        return memory.computeIfAbsent(loc, (x) -> 0L);
    }

    private void store(int loc, Long val) {
        memory.put(loc, val);
    }

    private void printCommand(String cmd, Parameter p1, Parameter p2, Parameter p3, long rbo, Command msg) {
        if (debugLog)
            System.out.println(String.format("'%s' %s \t[p1:%s, p2:%s, p3:%s, RBO:%s, PC:%s]", cmd, msg, p1, p2, p3, rbo, pc));
    }

    @SuppressWarnings("unused")
    public void setDebugLog(boolean debugLog) {
        this.debugLog = debugLog;
    }
}
