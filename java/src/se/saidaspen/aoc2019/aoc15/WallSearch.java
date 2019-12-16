package se.saidaspen.aoc2019.aoc15;

import se.saidaspen.aoc2019.aoc03.Point;

import java.util.List;
import java.util.Map;

import static se.saidaspen.aoc2019.aoc15.Aoc15.TILE_WALL;
import static se.saidaspen.aoc2019.aoc15.Aoc15.getAdjacent;

public class WallSearch implements SearchStrategy {


    @Override
    public int getNext(Point pos, int dir, Map<Point, Character> map) {
        List<Point> adjacent = getAdjacent(pos);

        //if (isWall(map, rightOf)) {
        //
       // }
        //return 0;
        return 0;
    }

    private boolean isWall(Map<Point, Character> map, Point rightOf) {
        return map.containsKey(rightOf) && TILE_WALL.equals(map.get(rightOf));
    }

    //north (1), south (2), west (3), and east (4)
    private Point rightOf(Point pos, int dir) {
        if (dir == 1) {
            return new Point(pos.x +1, pos.y);
        } else if (dir ==2){
            return new Point(pos.x -1, pos.y);
        } else if (dir == 3){
            return new Point(pos.x, pos.y+1);
        } else {
            return new Point(pos.x, pos.y-1);
        }
    }
}
