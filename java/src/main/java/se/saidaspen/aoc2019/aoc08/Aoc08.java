package se.saidaspen.aoc2019.aoc08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Aoc08 {

    private static final int TRANSPARENT = 2;

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        new Aoc08().run(input, 25, 6);
    }

    private void run(String input, int width, int height) {
        char[] chars = input.toCharArray();
        List<List<Integer>> layers = new ArrayList<>();
        int pixelsPerLayer = width * height;
        for (int i = 0; i < chars.length / pixelsPerLayer; i++) {
            List<Integer> layer = new ArrayList<>();
            int layerStart = i * pixelsPerLayer;
            int layerEnd = layerStart + pixelsPerLayer;
            for (int j = layerStart; j < layerEnd; j++) {
                layer.add(parseInt(Character.toString(chars[j])));
            }
            layers.add(layer);
        }
        integrityCheck(layers);
        render(layers, width, height);
    }

    private void integrityCheck(List<List<Integer>> layers) {
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
        System.out.println("#1 x #2 = " + (numOnes * numTwos));
    }

    private static void render(List<List<Integer>> layers, int width, int height) {
        for (int row = 0; row < height; row++) {
            StringBuilder rowString = new StringBuilder();
            for (int col = 0; col < width; col++) {
                rowString.append(getPixel(layers, row, col, width));
            }
            System.out.println(rowString.toString());
        }
    }

    private static String getPixel(List<List<Integer>> layers, int row, int col, int width) {
        int output = TRANSPARENT; // start transparent
        for (List<Integer> layer : layers) {
            output = output == TRANSPARENT ? layer.get(row * width + col) : output;
        }
        return (output == 1) ? " # " : "   ";
    }
}
