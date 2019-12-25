package se.saidaspen.aoc2019.aoc15;


import se.saidaspen.aoc2019.Point;

import java.util.Map;
import java.util.Random;

public class RandomSearch implements SearchStrategy {
    private final Random rn = new Random();

    @Override
    public int getNext(Point pos, int dir, Map<Point, Character> map) {
            return rn.nextInt(4) + 1;
    }
}
