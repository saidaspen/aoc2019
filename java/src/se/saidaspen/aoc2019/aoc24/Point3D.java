package se.saidaspen.aoc2019.aoc24;

import se.saidaspen.aoc2019.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Point class implementing Flyweight pattern.
 * Points are only ever created once for a certain pair of x, y coordinates.
 */
public final class Point3D {

    public final int x, y, z;
    private static final ConcurrentMap<String, Point3D> instances = new ConcurrentHashMap<>();


    @Override
    public String toString() {
        return String.format("(x=%s, y=%s, z=%s)", x, y, z);
    }

    @SuppressWarnings("unused")
    public static Point3D of(int x, int y, int z) {
        return instances.computeIfAbsent(keyOf(x, y, z), Point3D::new);
    }

    private static String keyOf(int x, int y, int z) {
        return "" + x + "," + y + "," + z;
    }

    // This will be quite slow for instantiation, but this only happens
    // once for a certain point. So that is a trade-off chosen.
    private Point3D(String key) {
        String[] split = key.split(",");
        // We don't have to worry about parsing causing problems, this method
        // is private and we are the only ones using it.
        x = Integer.parseInt(split[0]);
        y = Integer.parseInt(split[1]);
        z = Integer.parseInt(split[2]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point = (Point3D) o;
        return x == point.x && y == point.y && z == point.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public List<Point3D> getAdjacent() {
        ArrayList<Point3D> result = new ArrayList<>();
        if (x == 2 && y == 2) {
            return result;
        } else if (x == 2 && y == 1) {
            result.add(Point3D.of(0, 0, z-1));
            result.add(Point3D.of(1, 0, z-1));
            result.add(Point3D.of(2, 0, z-1));
            result.add(Point3D.of(3, 0, z-1));
            result.add(Point3D.of(4, 0, z-1));

            result.add(Point3D.of(x+1, y, z));

            result.add(Point3D.of(x-1, y, z));
            result.add(Point3D.of(x, y-1, z));
        } else if (x == 2 && y == 3) {
            result.add(Point3D.of(0, 4, z-1));
            result.add(Point3D.of(1, 4, z-1));
            result.add(Point3D.of(2, 4, z-1));
            result.add(Point3D.of(3, 4, z-1));
            result.add(Point3D.of(4, 4, z-1));

            result.add(Point3D.of(x+1, y, z));
            result.add(Point3D.of(x, y+1, z));
            result.add(Point3D.of(x-1, y, z));

        } else if (x == 1 && y == 2) {
            result.add(Point3D.of(0, 0, z-1));
            result.add(Point3D.of(0, 1, z-1));
            result.add(Point3D.of(0, 2, z-1));
            result.add(Point3D.of(0, 3, z-1));
            result.add(Point3D.of(0, 4, z-1));


            result.add(Point3D.of(x, y+1, z));
            result.add(Point3D.of(x-1, y, z));
            result.add(Point3D.of(x, y-1, z));
        } else if (x == 3 && y == 2) {
            result.add(Point3D.of(4, 0, z-1));
            result.add(Point3D.of(4, 1, z-1));
            result.add(Point3D.of(4, 2, z-1));
            result.add(Point3D.of(4, 3, z-1));
            result.add(Point3D.of(4, 4, z-1));

            result.add(Point3D.of(x+1, y, z));
            result.add(Point3D.of(x, y+1, z));

            result.add(Point3D.of(x, y-1, z));
        }
        else if (y == 0 && x== 0) {
            result.add(Point3D.of(x+1, y, z));
            result.add(Point3D.of(x, y+1, z));
            result.add(Point3D.of(1, 2, z+1));
            result.add(Point3D.of(2, 1, z+1));
        }
        else if (y == 0 && x== 4) {
            result.add(Point3D.of(3, 2, z+1));
            result.add(Point3D.of(x, y+1, z));
            result.add(Point3D.of(x-1, y, z));
            result.add(Point3D.of(2, 1, z+1));

        }
        else if (y == 4 && x== 0) {
            result.add(Point3D.of(x+1, y, z));
            result.add(Point3D.of(1, 2, z+1));

            result.add(Point3D.of(2, 3, z+1));

            result.add(Point3D.of(x, y-1, z));
        }
        else if (y == 4 && x== 4) {

            result.add(Point3D.of(2, 3, z+1));
            result.add(Point3D.of(3, 2, z+1));

            result.add(Point3D.of(x-1, y, z));
            result.add(Point3D.of(x, y-1, z));
        }
        else if (y == 0){
            result.add(Point3D.of(x+1, y, z));
            result.add(Point3D.of(x, y+1, z));
            result.add(Point3D.of(x-1, y, z));

            result.add(Point3D.of(2, 1, z+1));
        }   else if (y == 4){
            result.add(Point3D.of(x+1, y, z));

            result.add(Point3D.of(2, 3, z+1));

            result.add(Point3D.of(x-1, y, z));
            result.add(Point3D.of(x, y-1, z));
        }else if (x == 0){
            result.add(Point3D.of(x+1, y, z));
            result.add(Point3D.of(x, y+1, z));

            result.add(Point3D.of(1, 2, z+1));

            result.add(Point3D.of(x, y-1, z));
        }else if (x == 4){
            result.add(Point3D.of(3, 2, z+1));

            result.add(Point3D.of(x, y+1, z));
            result.add(Point3D.of(x-1, y, z));
            result.add(Point3D.of(x, y-1, z));
        }

        else {
            result.add(Point3D.of(x+1, y, z));
            result.add(Point3D.of(x, y+1, z));
            result.add(Point3D.of(x-1, y, z));
            result.add(Point3D.of(x, y-1, z));
        }
        return result;
    }

}
