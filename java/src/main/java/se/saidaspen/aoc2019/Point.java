package se.saidaspen.aoc2019;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Point class implementing Flyweight pattern.
 * Points are only ever created once for a certain pair of x, y coordinates.
 */
public final class Point {

    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;

    public final int x, y;
    private static final ConcurrentMap<String, Point> instances = new ConcurrentHashMap<>();


    @Override
    public String toString() {
        return String.format("(x=%s, y=%s)", x, y);
    }

    @SuppressWarnings("unused")
    public static Point of(int x, int y) {
        return instances.computeIfAbsent(keyOf(x, y), Point::new);
    }

    private static String keyOf(int x, int y) {
        return "" + x + "," + y;
    }

    // This will be quite slow for instantiation, but this only happens
    // once for a certain point. So that is a trade-off chosen.
    private Point(String key) {
        String[] split = key.split(",");
        // We don't have to worry about parsing causing problems, this method
        // is private and we are the only ones using it.
        x = Integer.parseInt(split[0]);
        y = Integer.parseInt(split[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Integer manhattanDistanceTo(Point to) {
        return Math.abs(x - to.x) + Math.abs(y - to.y);
    }

    public boolean isOnLine(Point lineStart, Point lineStop) {
        if (x == lineStart.x) {
            return (y < lineStart.y && y > lineStop.y) || (y > lineStart.y && y < lineStop.y);
        } else if (y == lineStart.y) {
            return (x < lineStart.x && x > lineStop.x) || (x > lineStart.x && x < lineStop.x);
        } else {
            return false;
        }
    }

    public Point[] getAdjacent() {
        Point[] result = new Point[4];
        result[SOUTH] = Point.of(x, y + 1);
        result[NORTH] = Point.of(x, y - 1);
        result[WEST] = Point.of(x - 1, y);
        result[EAST] = Point.of(x + 1, y);
        return result;
    }

}
