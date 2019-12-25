package se.saidaspen.aoc2019.aoc15;

import se.saidaspen.aoc2019.Point;

import java.util.Map;

public interface SearchStrategy {
    int getNext(Point pos, int dir, Map<Point, Character> map);
}
