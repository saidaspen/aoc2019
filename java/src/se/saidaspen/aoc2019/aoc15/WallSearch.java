package se.saidaspen.aoc2019.aoc15;

import se.saidaspen.aoc2019.aoc03.Point;

import java.util.Map;

import static se.saidaspen.aoc2019.aoc15.Aoc15.*;

public class WallSearch implements SearchStrategy {

    @Override
    public int getNext(Point pos, int dir, Map<Point, Character> map) {
        Point[] adjacent = getAdjacent(pos);
        //north (1), south (2), west (3), and east (4)
        int rightOf;
        int leftOf;
        int back;
        if (dir == 1) {
            back = 2;
            leftOf = 3;
            rightOf = 4;
        } else if (dir == 2) {
            back = 1;
            leftOf = 4;
            rightOf = 3;
        } else if (dir == 3) {
            back = 4;
            leftOf = 2;
            rightOf = 1;
        } else if (dir == 4) {
            back = 3;
            leftOf = 1;
            rightOf = 2;
        } else {
            throw new RuntimeException("Unsupported direction " + dir);
        }
        if (!isWall(map, adjacent[(4 + rightOf - 1) % 4])) {
            return rightOf;
        } else if (!isWall(map, adjacent[(4 + dir - 1) % 4])) {
            return dir;
        } else if (!isWall(map, adjacent[(4 + leftOf - 1) % 4])) {
            return leftOf;
        } else if ((!isWall(map, adjacent[(4 + back - 1) % 4]))) {
            return back;
        } else {
            throw new RuntimeException("Surrounded by walls!");
        }
    }

    private boolean isWall(Map<Point, Character> map, Point rightOf) {
        return map.containsKey(rightOf) && TILE_WALL.equals(map.get(rightOf));
    }
}
