package se.saidaspen.aoc2019.aoc03;

import java.util.Objects;

public final class Point {
    public final int x, y;

    @Override
    public String toString() {
        return String.format("(x=%s, y=%s)", x, y);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    Integer manhattanDistanceTo(Point to) {
        return Math.abs(x - to.x) + Math.abs(y - to.y);
    }

    boolean isOnLine(Point lineStart, Point lineStop) {
        if (x == lineStart.x) {
            return (y < lineStart.y && y > lineStop.y) || (y > lineStart.y && y < lineStop.y);
        } else if (y == lineStart.y) {
            return (x < lineStart.x && x > lineStop.x) || (x > lineStart.x && x < lineStop.x);
        } else {
            return false;
        }
    }

}
