package se.saidaspen.aoc2019.aoc09;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public final class IntComputer implements Runnable {

    private final Map<Integer, Long> memory;
    private final BlockingQueue<Long> in, out;
    private final int[] pAddrs = new int[3];

    private int pc = 0;
    private Long rbo = 0L;

    public IntComputer(Long[] code, BlockingQueue<Long> in, BlockingQueue<Long> out) {
        memory = new HashMap<>(code.length);
        for (int i = 0; i < code.length; i++) {
            memory.put(i, code[i]);
        }
        this.in = in;
        this.out = out;
    }

    public void run() {
        try {
            while(load(pc) != /*HALT*/ 99){
                var cmd = String.format("%05d", load(pc));
                var opCode = Integer.parseInt(cmd.substring(3));
                pAddrs[0] = getParam(cmd, 1);
                pAddrs[1] = getParam(cmd, 2);
                pAddrs[2] = getParam(cmd, 3);
                if (opCode == /*INPUT*/ 3) {
                    store(pAddrs[0], in.poll(10L, TimeUnit.DAYS));
                    pc += 2;
                } else if (opCode == /*OUTPUT*/ 4) {
                    out.put(load(pAddrs[0]));
                    pc += 2;
                } else if (opCode == /*ADJUST_RBO*/ 9) {
                    rbo += load(pAddrs[0]);
                    pc += 2;
                } else if (opCode == /*JMP_IF_TRUE*/ 5) {
                    pc = load(pAddrs[0]) > 0 ? load(pAddrs[1]).intValue() : pc + 3;
                } else if (opCode == /*JMP_IF_FALSE*/ 6) {
                    pc = load(pAddrs[0]) == 0 ? load(pAddrs[1]).intValue() : pc + 3;
                } else if (opCode == /*ADD*/ 1) {
                    store(pAddrs[2], load(pAddrs[0]) + load(pAddrs[1]));
                    pc += 4;
                } else if (opCode == /*MULT*/ 2) {
                    store(pAddrs[2], load(pAddrs[0]) * load(pAddrs[1]));
                    pc += 4;
                } else if (opCode == /*LESS_THAN*/ 7) {
                    store(pAddrs[2], load(pAddrs[0]) < load(pAddrs[1]) ? 1L : 0L);
                    pc += 4;
                } else if (opCode == /*EQUALS*/ 8) {
                    store(pAddrs[2], load(pAddrs[0]).equals(load(pAddrs[1])) ? 1L : 0L);
                    pc += 4;
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private int getParam(String cmd, int i) {
        switch (cmd.charAt(3 - i)) {
            case /*POSITION*/ '0': return load(pc + i).intValue();
            case /*IMMEDIATE*/'1': return pc + i;
            case /* RELATIVE */ '2': return Long.valueOf(load(pc + i) + rbo).intValue();
            default: throw new RuntimeException("Unsupported opmode");
        }
    }

    private Long load(int loc) { return memory.computeIfAbsent(loc, (x) -> 0L); }
    private void store(int loc, Long val) { memory.put(loc, val); }
}
