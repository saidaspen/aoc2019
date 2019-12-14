package se.saidaspen.aoc2019.aoc13;

import se.saidaspen.aoc2019.aoc09.IntComputer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

final class Aoc13 {

    private final Long[] code;

    public static void main(String[] args) throws Exception {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        new Aoc13(input).part2();
    }

    private Aoc13(String input) {
        code = Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
    }

    @SuppressWarnings("unused")
    private void part1() throws Exception {
        int[][] board = runGame();
        int blockCnt = 0;
        for (int[] rows : board) {
            IntStream.range(0, board[0].length).mapToObj(j -> rows[j] == 2).count();
            for (int j = 0; j < board[0].length; j++) {
                if (rows[j] == 2) {
                    blockCnt++;
                }
            }
        }
        System.out.println(blockCnt);
    }

    private void part2() throws Exception {
        code[0] = 2L;
        runGame();
    }

    private int[][] runGame() throws InterruptedException {
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10000);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
        IntComputer cpu = new IntComputer(code, in, out);
        Game game = new Game(out, in);
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        pool.execute(cpu);
        pool.execute(game);
        pool.shutdown();
        pool.awaitTermination(100L, TimeUnit.DAYS);
        return game.board;
    }

    private static class Game implements Runnable {
        private final ArrayBlockingQueue<Long> out;
        private final ArrayBlockingQueue<Long> in;
        private final int width = 100;
        private final int height = 22;
        private boolean stopped = false;

        private int score;
        int[][] board = new int[height][width];
        private final Thread updaterThread;

        Game(ArrayBlockingQueue<Long> in, ArrayBlockingQueue<Long> out) {
            this.out = out;
            this.in = in;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    board[i][j] = 0;
                }
            }
            Runnable updater = new BoardUpdater(this);
            updaterThread = new Thread(updater);
            updaterThread.start();
        }

        @Override
        public void run() {
            try {
                while (!stopped) {
                    Thread.sleep(45L);
                    draw();
                    int ballX = 0;
                    int padX = 0;
                    int joystick = 0;
                    for (int row = 0; row < height; row++) {
                        for (int col = 0; col < width; col++) {
                            int val = board[row][col];
                            ballX = val == 4 ? col : ballX;
                            padX = val == 3 ? col : padX;
                        }
                    }
                    if (padX > ballX) {
                        joystick -= 1;
                    } else if (padX < ballX) {
                        joystick += 1;
                    }
                    out.put((long) joystick);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void draw() {
            StringBuilder sb = new StringBuilder();
            for (int[] rows : board) {
                for (int col = 0; col < board[0].length; col++) {
                    sb.append(toPixel(rows[col]));
                }
                sb.append("\n");
            }
            System.out.print("\033[2J"); // Clear screen
            System.out.println(sb);
            System.out.println("Score: " + score);
        }

        private char toPixel(Integer id) {
            if (id == 1) {
                return '█'; //wall
            } else if (id == 2) {
                return '▢'; //block
            } else if (id == 3) {
                return '▔';
            } else if (id == 4) {
                return '●'; //ball
            } else {
                return ' ';
            }
        }
    }

    private static class BoardUpdater implements Runnable {
        private final Game game;

        BoardUpdater(Game game) {
            this.game = game;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    int x = 0;
                    int y = 0;
                    for (int i = 0; i < 3; i++) {
                        Long oVal = game.in.poll(5, TimeUnit.SECONDS);
                        if (oVal == null) {
                            game.stopped = true;
                            return;
                        }
                        if (i == 0) {
                            x = oVal.intValue();
                        } else if (i == 1) {
                            y = oVal.intValue();
                        } else {
                            if (x == -1) {
                                game.score = oVal.intValue();
                            } else {
                                game.board[y][x] = oVal.intValue();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
