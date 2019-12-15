package se.saidaspen.aoc2019.aoc15;

import se.saidaspen.aoc2019.aoc03.Point;
import se.saidaspen.aoc2019.aoc09.IntComputer;

import java.io.IOException;
import java.io.PipedInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Aoc15 {

    private static final boolean PRINT = true;
    private final Long[] code;
    private static Map<Point, Character> map = new HashMap<>();
    private static Long[] display = new Long[]{0L, 0L, 0L, 0L};

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        new Aoc15(input).part1();
    }

    Aoc15(String input) {
        code = Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
    }

    void part1() throws InterruptedException {
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10000);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
        IntComputer cpu = new IntComputer(code, in, out);
        RepairBot robot = new RepairBot(0, 0, out, in);
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        pool.execute(cpu);
        pool.execute(robot);
        pool.shutdown();
        pool.awaitTermination(100L, TimeUnit.DAYS);
        printMap(map);
        simulateOxygenSpread(map);
    }

    private void simulateOxygenSpread(Map<Point, Character> map) throws InterruptedException {
        display[3] = 2L;
        Point oxygenStartPos = find(map, 'X').get(0);
        map.put(oxygenStartPos, 'O');
        List<Point> oxygenPoints = new ArrayList<>();
        Long cnt = 1L;
        do {
            Thread.sleep(20L);
            oxygenPoints = find(map, 'O');
            for (Point p : oxygenPoints) {
                List<Point> adjacent = getAdjacent(p);
                for (Point adj : adjacent) {
                    if (map.containsKey(adj) && (map.get(adj).equals('R') || map.get(adj).equals('.') || map.get(adj).equals(' '))) {
                        map.put(adj, 'O');
                    }
                }
            }
            cnt++;
            printMap(map);
            display[2] = cnt;
        } while(!isFilled());
    }

    private boolean isFilled() {
        for (Character c : map.values()) {
            if (c.equals('.')) {
                return false;
            }
        }
        return true;
    }

    private static List<Point> getAdjacent(Point p) {
        List<Point> result = new ArrayList<>();
        result.add(new Point(p.x + 1, p.y));
        result.add(new Point(p.x - 1, p.y));
        result.add(new Point(p.x, p.y + 1));
        result.add(new Point(p.x, p.y - 1));
        return result;
    }

    private static List<Point> find(Map<Point, Character> map, char x) {
        List<Point> points = new ArrayList<>();
        for (Point p : map.keySet()) {
            if (map.get(p).equals(x)) {
                points.add(p);
            }
        }
        return points;
    }

    private static void printMap(Map<Point, Character> panelColors) {
        if (!PRINT){
            return;
        }
        System.out.print("\033[2J"); // Clear screen
        Map<Point, Character> canvas = reorient(panelColors);
        var largestX = 0;
        var largestY = 0;
        for (Point p : canvas.keySet()) {
            largestX = Math.max(p.x, largestX);
            largestY = Math.max(p.y, largestY);
        }
        List<List<Character>> lines = emptyLines(largestX, largestY);
        for (Point p : canvas.keySet()) {
            lines.get(p.y).set(p.x, canvas.get(p));
        }
        render(lines);
        String mode = "";
        if (display[3] == 0) {
            mode = "SEARCH OXYGEN";
        } else if (display[3] == 1) {
            mode = "FILL MAP";
        } else if (display[3] == 2) {
            mode = "FILL OXYGEN";
        }
        System.out.println("Robot steps: " + display[0]);
        System.out.println("Closest path: " + display[1]);
        System.out.println("Oxygen spread (s): " + display[2]);
        System.out.println("Mode: " + mode);
    }

    private static List<List<Character>> emptyLines(int xMax, int yMax) {
        List<List<Character>> lines = new ArrayList<>();
        for (int row = 0; row <= yMax; row++) {
            List<Character> r = new ArrayList<>();
            for (int col = 0; col <= xMax; col++) {
                r.add(' ');
            }
            lines.add(r);
        }
        return lines;
    }
    private static Map<Point, Character> reorient(Map<Point, Character> map) {
        Map<Point, Character> outMap = new HashMap<>();
        int minX = 0;
        int minY = 0;
        for (Point p : map.keySet()) {
            minX = Math.min(p.x, minX);
            minY = Math.min(p.y, minY);
        }
        for (Point p : map.keySet()) {
            outMap.put(new Point(p.x + Math.abs(minX), p.y + Math.abs(minY)), map.get(p));
        }
        return outMap;
    }

    private static void render(List<List<Character>> lines) {
        for (int i = lines.size() - 1; i >= 0; i--) {
            List<Character> row = lines.get(i);
            StringBuilder sb = new StringBuilder();
            for (Character c : row) {
                sb.append(c);
            }
            System.out.println(sb);
        }
    }

    private static class RepairBot implements Runnable {
        private int x, y;
        private int dir = 1;
        private ArrayBlockingQueue<Long> input, output;
        long step = 0;
        private final Random rn = new Random();
        private final Stack<Point> log = new Stack();
        private Point oxygenPos;

        public RepairBot(int x, int y, ArrayBlockingQueue<Long> input, ArrayBlockingQueue<Long> output) {
            this.x = x;
            this.y = y;
            this.input = input;
            this.output = output;
            map.put(new Point(x, y), 'R');
        }

        @Override
        public void run() {
            try {
                while (!mapKnown()) {
                    step++;
                    display[0] = step;
                    if (step %100 == 0 ) {
                        printMap(map);
                    }
                    Point currPos = new Point(x, y);
                    if (log.contains(currPos)) {
                        while(!log.pop().equals(currPos)) {

                        }
                    }
                    log.push(currPos);
                    output.put((long) dir); // Send dir
                    Long status = input.poll(5, TimeUnit.SECONDS);
                    int nextDir = randInput();//getInput();
                    if (status == null ){
                        return;
                    } else if (status == 0) {
                        updateMap('█');
                    } else if (status == 1) {
                        move();
                    } else if (status == 2) {
                        updateMap('X');
                        oxygenPos = find(map, 'X').get(0);
                        display[1] = (long) log.size();
                        display[3] = 1L;
                        move();
                    }
                    dir = nextDir;

                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        private boolean mapKnown() {
            boolean hasExplored = false;
            for (Point p : map.keySet()) {
                if (Character.valueOf('.').equals(map.get(p)) || Character.valueOf('R').equals(map.get(p))) {
                    hasExplored = true;
                    List<Point> adjacent = getAdjacent(p);
                    for (Point adj : adjacent) {
                        Character adjChar = map.get(adj);
                        if (adjChar == null || Character.valueOf(' ').equals(adjChar)) {
                            return false;
                        }
                    }
                }
            }
            return hasExplored;
        }

        private int randInput() {
            return rn.nextInt(4) + 1;
        }

        private int getInput() {
            Scanner in = new Scanner(System.in);
            while(true) {
                String s = in.nextLine();
                // north (1), south (2), west (3), and east (4).
                if (s.equalsIgnoreCase("a")) {
                    return 3;
                } else if (s.equalsIgnoreCase("s")) {
                    return 2;
                } else if (s.equalsIgnoreCase("d")) {
                    return 4;
                } else if (s.equalsIgnoreCase("w")) {
                    return 1;
                }
                System.out.println("Only 1-4 allowed");
            }
        }

        private void updateMap(char tile) {
            if (dir == 1) {
                map.put(new Point(x, y+1), tile);
            } else if (dir == 2){
                map.put(new Point(x, y-1), tile);
            } else if (dir == 3) {
                map.put(new Point(x-1, y), tile);
            } else if (dir == 4) {
                map.put(new Point(x+1, y), tile);
            }
        }

        private void move() {
            map.remove(new Point(x, y));
            if (x == 0 && y == 0) {
                map.put(new Point(x, y), 'S');
            } else if (oxygenPos != null && x == oxygenPos.x && y == oxygenPos.y){
                map.put(new Point(x, y), 'X');
            } else {
                map.put(new Point(x, y), '.');
            }
            if (dir == 1) {
                this.y++;
            } else if (dir == 2){
                this.y--;
            } else if (dir == 3) {
                this.x--;
            } else if (dir == 4) {
                this.x++;
            }
            map.put(new Point(x, y), 'R');
        }
    }
}
