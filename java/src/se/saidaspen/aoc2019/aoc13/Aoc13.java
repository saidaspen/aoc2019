package se.saidaspen.aoc2019.aoc13;

import se.saidaspen.aoc2019.aoc09.IntComputer;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

final class Aoc13 {


    private final Long[] code;

    public static void main(String[] args) throws Exception {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        System.out.println(new Aoc13(input).part2());
    }

    Aoc13(String input) {
        List<String> lines = Arrays.stream(input.split("\n"))
                .map(String::trim).collect(Collectors.toList());
        code = Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
    }

    private static class Tile {
        int x, y, id;
    }

    public String part2() throws Exception {
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10000);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
        //Long starVal = 2L;
        //in.put(starVal);
        code[0] = 2L;
        IntComputer cpu = new IntComputer(code, in, out);
        Game game = new Game(out, in);
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        pool.execute(cpu);
        pool.execute(game);
        pool.shutdown();
        pool.awaitTermination(100L, TimeUnit.DAYS);
        return "done";
    }

    public String part1() throws Exception {
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10000);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
        //Long starVal = 1L;
        //in.put(starVal);
        IntComputer cpu = new IntComputer(code, in, out);
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        pool.execute(cpu);
        pool.shutdown();
        pool.awaitTermination(100L, TimeUnit.DAYS);
        List<Long> outvals = new ArrayList<>();
        while(true) {
            Long oVal = out.poll(1, TimeUnit.SECONDS);
            if (oVal == null)
                break;
            else
                outvals.add(oVal);
        }

        List<Tile> tiles = new ArrayList<>();
        Tile tile = new Tile();
        for (int i = 0; i < outvals.size(); i++) {
            if (i%3 == 0) {
                tile = new Tile();
                tile.x = outvals.get(i).intValue();
            }
            else if (i%3 == 1) {
                tile.y = outvals.get(i).intValue();
            }
            else if (i%3 == 2) {
                tile.id = outvals.get(i).intValue();
                tiles.add(tile);
            }
        }

        System.out.println("Tiles size:" + tiles.size());
        System.out.println("outvals size:" + outvals.size());
        System.out.println(tiles.stream().filter(t -> t.id == 2).count());
        return "";
    }

    private class Game implements Runnable {
        private final ArrayBlockingQueue<Long> out;
        private final ArrayBlockingQueue<Long> in;
        int width = 100;
        private Long score;
        private int height = 30;
        int[][] board = new int[height][width];
        String clearScreen;
        int jPos = 0;

        public Game(ArrayBlockingQueue<Long> out, ArrayBlockingQueue<Long> in) {
            this.out = out;
            this.in = in;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    board[i][j] = 0;
                }
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < (width + 1) * height; i++) {
                sb.append('\b');
            }
            clearScreen = sb.toString();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println(score);
                    List<Tile> tiles = getTiles();
                    int ballX = 0, ballY= 0;
                    int padelX = 0, padelY= 0;
                    int joystick = 0;
                    for (Tile t : tiles) {
                        if (t.id == 4) {
                            ballX = t.x;
                            ballY = t.y;
                        }
                        if (t.id == 3) {
                            padelX = t.x;
                            padelY = t.y;
                        }
                    }
                    if (padelX > ballX) {
                        joystick = -1;
                    } else if (padelX < ballX) {
                        joystick = 1;
                    }
                    out.put((long) joystick);

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private List<Tile> getTiles() throws InterruptedException {
            List<Long> outvals = new ArrayList<>();
            while (true) {
                Long oVal = out.poll(300, TimeUnit.MILLISECONDS);
                if (oVal == null)
                    break;
                else
                    outvals.add(oVal);
            }

            List<Tile> tiles = new ArrayList<>();
            Tile tile = new Tile();
            boolean rs = false;
            for (int i = 0; i < outvals.size(); i++) {
                Long val = outvals.get(i);
                if (i % 3 == 0) {
                    if (val == -1) {
                        rs = true;
                    } else {
                        tile = new Tile();
                        tile.x = val.intValue();
                    }
                } else if (i % 3 == 1) {
                    if (!rs)
                        tile.y = val.intValue();
                } else if (i % 3 == 2) {
                    if (rs) {
                        this.score = val;
                    } else {
                        tile.id = val.intValue();
                        tiles.add(tile);
                    }
                }
            }
            return tiles;
        }

    }
}
