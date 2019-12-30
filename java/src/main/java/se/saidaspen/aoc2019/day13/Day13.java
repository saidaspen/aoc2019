package se.saidaspen.aoc2019.day13;

import se.saidaspen.aoc2019.AocUtil;
import se.saidaspen.aoc2019.Day;
import se.saidaspen.aoc2019.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Solution to Advent of Code 2019 Day 13
 * The original puzzle can be found here: https://adventofcode.com/2019/day/13
 */
public final class Day13 implements Day {
    private static final char BLOCK = '█';
    private final Long[] code;

    Day13(String input) {
        code = AocUtil.toLongCode(input);
    }

    public String part1() {
        Long[] codeCopy = new Long[code.length];
        System.arraycopy(code, 0, codeCopy, 0, code.length);
        codeCopy[0] = 2L;
        Game game = new Game(codeCopy);
        game.run();
        return Long.toString(game.cntBlocksStart);
    }

    public String part2() {
        Long[] codeCopy = new Long[code.length];
        System.arraycopy(code, 0, codeCopy, 0, code.length);
        codeCopy[0] = 2L;
        Game game = new Game(codeCopy);
        game.run();
        return Integer.toString(game.score);
    }

    private static class Game implements Runnable {
        /* CPU STUFF */
        private final Map<Integer, Long> memory;
        private final int[] pAddresses = new int[3];
        private int pc = 0;
        private Long rbo = 0L;

        /* GAME STUFF */
        private final char[] PIXELS = new char[]{' ', BLOCK, '▢', '▔', '●'};
        private int score;
        Map<Point, Character> board = new HashMap<>();
        int paramCnt = 0;
        private Point padPos = null;
        private Point ballPos = null;
        private int x;
        private int y;
        private long cntBlocksStart = 0;

        public Game(Long[] code) {
            memory = new HashMap<>(code.length);
            for (int i = 0; i < code.length; i++) {
                memory.put(i, code[i]);
            }
        }

        @Override
        public void run() {
            while (!finished()) {
                try {
                    while (load(pc) != /*HALT*/ 99) {
                        var cmd = String.format("%05d", load(pc));
                        var opCode = Integer.parseInt(cmd.substring(3));
                        pAddresses[0] = getParam(cmd, 1); // Param 1
                        pAddresses[1] = getParam(cmd, 2); // Param 2
                        pAddresses[2] = getParam(cmd, 3); // Param 3
                        if (opCode == /*INPUT*/ 3) {
                            Long val = getUserInput();
                            store(pAddresses[0], val);
                            pc += 2;
                        } else if (opCode == /*OUTPUT*/ 4) {
                            output(load(pAddresses[0]).intValue());
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
                }
            }
        }

        private boolean finished() {
            if (padPos == null || ballPos == null) {
                return false;
            } else if (ballPos.y < padPos.y) {
                return true;
            }
            return !board.containsValue(BLOCK);
        }

        private long getUserInput() {
            return Integer.compare(ballPos.x, padPos.x);
        }

        private void output(Integer oVal) {
            if (oVal == null) {
                return;
            }
            if (this.paramCnt == 0) {
                x = oVal;
            } else if (paramCnt == 1) {
                y = oVal;
            } else {
                if (x == -1) {
                    score = oVal;
                } else {
                    if (oVal == 4) {
                        ballPos = Point.of(x, y);
                    } else if (oVal == 3) {
                        padPos = Point.of(x, y);
                    } else if (oVal == 2) {
                        cntBlocksStart++;
                    }
                    board.put(Point.of(x, y), PIXELS[oVal]);
                }
            }
            paramCnt = (paramCnt + 1) % 3;
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
                    throw new RuntimeException("Unsupported op mode");
            }
        }

        private Long load(int loc) {
            return memory.computeIfAbsent(loc, (x) -> 0L);
        }

        private void store(int loc, Long val) {
            memory.put(loc, val);
        }
    }
}
