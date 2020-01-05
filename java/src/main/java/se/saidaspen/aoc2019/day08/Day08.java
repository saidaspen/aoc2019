package se.saidaspen.aoc2019.day08;

import se.saidaspen.aoc2019.Day;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Solution to Advent of Code 2019 Day 8
 * The original puzzle can be found here: https://adventofcode.com/2019/day/8
 */
public final class Day08 implements Day {

    private static final int WIDTH = 25;
    private static final int HEIGHT = 6;
    private static final int TRANSPARENT = 2;
    private final List<List<Integer>> layers;

    public Day08(String input) {
        layers = toLayers(input);
    }

    private List<List<Integer>> toLayers(String input) {
        char[] chars = input.toCharArray();
        List<List<Integer>> layers = new ArrayList<>();
        int pixelsPerLayer = WIDTH * HEIGHT;
        for (int i = 0; i < chars.length / pixelsPerLayer; i++) {
            List<Integer> layer = new ArrayList<>();
            int layerStart = i * pixelsPerLayer;
            for (int j = layerStart; j < layerStart + pixelsPerLayer; j++) {
                layer.add(parseInt(Character.toString(chars[j])));
            }
            layers.add(layer);
        }
        return layers;
    }

    private long integrityCheck(List<List<Integer>> layers) {
        int layerWithFewest = 0;
        int fewest = Integer.MAX_VALUE;
        for (int i = 0; i < layers.size(); i++) {
            long numZeros = layers.get(i).stream().filter(val -> val == 0).count();
            if (numZeros < fewest) {
                fewest = (int) numZeros;
                layerWithFewest = i;
            }
        }
        long numOnes = layers.get(layerWithFewest).stream().filter(val -> val == 1).count();
        long numTwos = layers.get(layerWithFewest).stream().filter(val -> val == 2).count();
        return numOnes * numTwos;
    }

    private static String render(List<List<Integer>> layers) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < HEIGHT; row++) {
            StringBuilder rowString = new StringBuilder();
            for (int col = 0; col < WIDTH; col++) {
                rowString.append(getPixel(layers, row, col));
            }
            sb.append(rowString.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private static String getPixel(List<List<Integer>> layers, int row, int col) {
        int output = TRANSPARENT; // start transparent
        for (List<Integer> layer : layers) {
            output = output == TRANSPARENT ? layer.get(row * WIDTH + col) : output;
        }
        return (output == 1) ? " # " : "   ";
    }

    @Override
    public String part1() {
        return Long.toString(integrityCheck(layers));
    }

    @Override
    public String part2() {
        return render(layers);
    }
}
