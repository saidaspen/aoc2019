package se.saidaspen.aoc2019.aoc17;

import se.saidaspen.aoc2019.AocUtil;
import se.saidaspen.aoc2019.IntComputer;
import se.saidaspen.aoc2019.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Aoc17 {

    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    private static final List<Character> dirSymbols = Arrays.asList('^', '>', 'v', '<');

    private final Long[] code;
    private int dir;
    private Point startPos;

    public static void main(String[] args) throws IOException, InterruptedException {
        Aoc17 app = new Aoc17(AocUtil.input(17));
        app.run();
    }

    Aoc17(String input) {
        code = Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
    }

    private static Map<Point, Character> map = new HashMap<>();

    // The first step is to calibrate the cameras by getting the alignment parameters of some well-defined points. Locate all scaffold intersections; for each, its alignment parameter is the distance between its left edge and the left edge of the view multiplied by the distance between its top edge and the top edge of the view. Here, the intersections from the above image are marked O:
    void run() throws InterruptedException {
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(1);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
        code[0] = 2L;
        IntComputer cpu = new IntComputer(code, in, out);
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        pool.execute(cpu);
        do {
            Thread.sleep(100L);
        } while (cpu.status() == IntComputer.Status.RUNNING);
        fillMap(out);
        printAlignment();
        printMap(map);
        Stack<Point> path = findPath();
        assert path != null;
        List<String> cmds = toCommands(path);
        String cmdString = String.join(",", cmds);
        String encoded = encode(cmdString);
        List<String> partitions = partition(encoded, 20);
        assert partitions != null;
        partitions = partitions.stream().map(this::decode).collect(Collectors.toList());
        List<String> commands = new ArrayList<>();
        String left = cmdString.replaceAll(",", "");
        while (left.length() > 0) {
            if (left.startsWith(partitions.get(0))) {
                commands.add("A");
                left = left.substring(partitions.get(0).length());
            } else if (left.startsWith(partitions.get(1))) {
                commands.add("B");
                left = left.substring(partitions.get(1).length());
            } else if (left.startsWith(partitions.get(2))) {
                commands.add("C");
                left = left.substring(partitions.get(2).length());
            } else {
                throw new RuntimeException("Partitioning was wrong!");
            }
        }
        List<String> subCommands = partitions.stream().map(this::toCommandList).collect(Collectors.toList());
        putList(in, Arrays.toString(commands.toArray()).replaceAll("[ \\[\\]]", ""));
        for (String subs : subCommands) {
            putList(in, subs);
        }
        in.put((long) 'n');
        in.put(10L);
        do {
            Thread.sleep(100L);
        } while (cpu.status() == IntComputer.Status.RUNNING);

        Long val = 0L;
        while (out.peek() != null) {
            val = out.poll();
        }
        System.out.println("Final score: " + val);
        pool.shutdown();
        pool.awaitTermination(100L, TimeUnit.SECONDS);
    }

    private void printAlignment() {
        List<Point> crossings = crossings();
        int alignment = 0;
        for (Point p : crossings) {
            alignment += p.x * p.y;
        }
        System.out.println("Alignment: " + alignment);
    }

    private void putList(ArrayBlockingQueue<Long> in, String cmds) throws InterruptedException {
        for (char c : cmds.toCharArray()) {
            in.put((long) (int) c);
        }
        in.put(10L);
    }

    private String toCommandList(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            sb.append(c);
            if (i < s.length() - 1 && !(Character.toString(c).matches("\\d+") && Character.toString(s.charAt(i + 1)).matches("\\d+"))) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private List<String> partition(String in, int maxLen) {
        List<String> splits = new ArrayList<>();
        String left = in;
        List<int[]> lengths = new ArrayList<>();
        for (int i = 1; i <= maxLen; i++) {
            for (int j = 1; j <= maxLen; j++) {
                for (int k = 1; k <= maxLen; k++) {
                    lengths.add(new int[]{i, j, k});
                }
            }
        }
        for (int[] l : lengths) {
            // A
            String a = left.substring(0, Math.min(l[0], left.length()));
            splits.add(a);
            if (l[0] >= left.length()) {
                splits.clear();
                left = in;
                continue;
            }
            left = left.substring(l[0]);
            while (left.indexOf(a) == 0) {
                left = left.substring(a.length());
            }

            // B
            String b = left.substring(0, Math.min(l[1], left.length()));
            splits.add(b);
            if (l[1] >= left.length()) {
                splits.clear();
                left = in;
                continue;
            }
            left = left.substring(l[1]);
            while (left.startsWith(a) || left.startsWith(b)) {
                if (left.startsWith(a)) {
                    left = left.substring(a.length());
                } else if (left.startsWith(b)) {
                    left = left.substring(b.length());
                }
            }

            // C
            String c = left.substring(0, Math.min(l[2], left.length()));
            splits.add(c);
            if (l[2] >= left.length()) {
                splits.clear();
                left = in;
                continue;
            }
            left = left.substring(l[2]);
            while (left.startsWith(a) || left.startsWith(b) || left.startsWith(c)) {
                if (left.startsWith(a)) {
                    left = left.substring(a.length());
                } else if (left.startsWith(b)) {
                    left = left.substring(b.length());
                } else if (left.startsWith(c)) {
                    left = left.substring(c.length());
                }
            }

            if (left.length() == 0) {
                return splits;
            } else {
                splits.clear();
                left = in;
            }
        }
        return null;
    }

    private String encode(String original) {
        String[] split = original.split(",");
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            if (s.matches("\\d+")) {
                sb.append((char) ('A' - 1 + Integer.parseInt(s)));
            } else if (s.equals("R")) {
                sb.append(">");
            } else if (s.equals("L")) {
                sb.append("<");
            }
        }
        return sb.toString();
    }

    private String decode(String s) {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '>') {
                sBuilder.append("R");
            } else if (c == '<') {
                sBuilder.append("L");
            } else {
                sBuilder.append((int) c - 'A' + 1);
            }
        }
        return sBuilder.toString();
    }

    private List<String> toCommands(Stack<Point> path) {
        Stack<String> commands = new Stack<>();
        Point currPos = startPos;
        int currDir = dir;
        for (Point p : path) {
            if (p == startPos) {
                continue;
            }
            Turn turn = getTurn(currPos, p, currDir);
            if (turn == Turn.FORWARD) {
                String lastCommand = commands.pop();
                Integer steps = getNumber(lastCommand);
                if (steps == null) {
                    commands.push(lastCommand);
                    commands.push("1");
                } else {
                    commands.push(Integer.toString(steps + 1));
                }
            } else if (turn == Turn.RIGHT) {
                commands.push("R");
                commands.push("1");
                currDir = (currDir + 1) % 4;
            } else if (turn == Turn.LEFT) {
                commands.push("L");
                commands.push("1");
                currDir = Math.floorMod(currDir - 1, 4);
            }
            currPos = p;
        }
        return commands;
    }

    private Stack<Point> findPath() {
        List<Point> scaffolds = find(map, '#');
        Stack<Point> posLog = new Stack<>();
        Point pos = startPos;
        int currDir = dir;
        Point next;
        while (true) {
            Point[] adjacentPoints = pos.getAdjacent();
            List<Point> candidates = new ArrayList<>(4);
            for (Point p : adjacentPoints) {
                if (p != null) {
                    if (Character.valueOf('#').equals(map.get(p)) && (posLog.isEmpty() || !p.equals(posLog.peek()))) {
                        candidates.add(p);
                    }
                }
            }
            if (candidates.size() == 1) {
                next = candidates.get(0);
            } else {
                next = move(pos, currDir);
            }
            posLog.add(pos);

            if (posLog.size() > 20 * 20) {
                return null;
            }
            if (allScaffoldsCovered(posLog, scaffolds)) {
                return posLog;
            }
            Turn turn = getTurn(pos, next, currDir);
            if (turn == Turn.RIGHT) {
                currDir = (currDir + 1) % 4;
            } else if (turn == Turn.LEFT) {
                currDir = Math.floorMod(currDir - 1, 4);
            }
            pos = next;
        }
    }

    private Point move(Point p, int currDir) {
        if (currDir == NORTH) {
            return Point.of(p.x, p.y - 1);
        } else if (currDir == EAST) {
            return Point.of(p.x + 1, p.y);
        } else if (currDir == SOUTH) {
            return Point.of(p.x, p.y + 1);
        } else if (currDir == WEST) {
            return Point.of(p.x - 1, p.y);
        } else {
            throw new RuntimeException("Unsupporte dir " + currDir);
        }
    }

    private boolean allScaffoldsCovered(List<Point> posLog, List<Point> scaffolds) {
        for (Point p : scaffolds) {
            if (!posLog.contains(p))
                return false;
        }
        return true;
    }

    private Integer getNumber(String lastCommand) {
        try {
            return Integer.parseInt(lastCommand);
        } catch (Exception ignored) {
            return null;
        }
    }

    private Turn getTurn(Point from, Point to, int currDir) {
        if (currDir == NORTH) {
            return to.x == from.x ? Turn.FORWARD : to.x > from.x ? Turn.RIGHT : Turn.LEFT;
        } else if (currDir == SOUTH) {
            return to.x == from.x ? Turn.FORWARD : to.x < from.x ? Turn.RIGHT : Turn.LEFT;
        } else if (currDir == WEST) {
            return to.y == from.y ? Turn.FORWARD : to.y > from.y ? Turn.LEFT : Turn.RIGHT;
        } else if (currDir == EAST) {
            return to.y == from.y ? Turn.FORWARD : to.y > from.y ? Turn.RIGHT : Turn.LEFT;
        } else {
            throw new RuntimeException("Strange direction " + currDir);
        }
    }

    private enum Turn {
        LEFT, RIGHT, FORWARD
    }

    private List<Point> crossings() {
        List<Point> crossings = new ArrayList<>();
        for (Point p : map.keySet()) {
            Point[] adjacent = p.getAdjacent();
            boolean crossing = true;
            if (!Character.valueOf('#').equals(map.get(p))) {
                continue;
            }
            for (Point adj : adjacent) {
                if (!Character.valueOf('#').equals(map.get(adj))) {
                    crossing = false;
                    break;
                }
            }
            if (crossing) {
                crossings.add(p);
            }
        }
        return crossings;
    }

    private void fillMap(ArrayBlockingQueue<Long> out) throws InterruptedException {
        int currRow = 0;
        int currCol = 0;
        Long output = out.poll(3, TimeUnit.SECONDS);
        while (output != null) {
            char c = (char) output.byteValue();
            Point pos = Point.of(currCol, currRow);
            if (dirSymbols.contains(c)) {
                dir = dirSymbols.indexOf(c);
                startPos = pos;
                c = '#';
            }
            if (c == 10) {
                currRow++;
                currCol = 0;
            } else {
                map.put(pos, c);
                currCol++;
            }
            output = out.poll(3, TimeUnit.SECONDS);
        }
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

    private static void printMap(Map<Point, Character> map) {
        System.out.print("\033[2J"); // Clear screen
        int largestX = 0;
        int largestY = 0;
        for (Point p : map.keySet()) {
            largestX = Math.max(p.x, largestX);
            largestY = Math.max(p.y, largestY);
        }
        List<List<Character>> lines = emptyLines(largestX, largestY);
        for (Point p : map.keySet()) {
            lines.get(p.y).set(p.x, map.get(p));
        }
        render(lines);
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

    private static void render(List<List<Character>> lines) {
        for (List<Character> row : lines) {
            StringBuilder sb = new StringBuilder();
            for (Character c : row) {
                sb.append(c);
            }
            System.out.println(sb);
        }
    }
}
