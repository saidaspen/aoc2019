package se.saidaspen.aoc2019.aoc06;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.toMap;

public class Aoc06 {

    private final Map<String, String> orbits;

    private Aoc06(Map<String, String> orbits) {
        this.orbits = orbits;
    }

    public static void main(String[] args) throws IOException {
        new Aoc06(lines(Paths.get(args[0])).map(l -> l.split("\\)")).collect(toMap(l -> l[1], l -> l[0]))).run();
    }

    private void run() {
        int cnt = orbits.keySet().parallelStream().map(this::pathToCOM).mapToInt(List::size).sum();
        LinkedList<String> youPath = pathToCOM("YOU");
        LinkedList<String> sanPath = pathToCOM("SAN");
        while (youPath.getLast().equals(sanPath.getLast())) {
            youPath.removeLast();
            sanPath.removeLast();
        }
        System.out.println(String.format("Total:%d. YOU->SAN:%d", cnt, (youPath.size() + sanPath.size())));
    }

    private LinkedList<String> pathToCOM(String from) {
        String inner = from;
        LinkedList<String> path = new LinkedList<>();
        while ((inner = orbits.get(inner)) != null) {
            path.add(inner);
        }
        return path;
    }
}
