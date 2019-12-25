package se.saidaspen.aoc2019.aoc25;

import se.saidaspen.aoc2019.IntComputer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.time.temporal.ChronoUnit.MILLIS;

public final class Aoc25 {

    private String code;

    // Build nodes of rooms
    // Perform BFS
    //  Find items
    //  If an item halts program add to ignore list
    //  Find path-way to pressure sending door
    // Once all is explored pickup all items
    // Go to presurre sensor room
    // find all combination of items
    // Test each one
    // if You get the code return it
    //
    // right answer for me was:
    // 134807554
    // for items:
    // - ornament
    //- astrolabe
    //- sand
    //- shell
    Random rnd = new Random();
    List<String> results;
    List<String> rooms = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        Aoc25 app = new Aoc25(new String(Files.readAllBytes(Paths.get(args[0]))));
        app.multiplay();

    }

    private void multiplay() throws IOException, InterruptedException {
        while (true) {
            String result = play();
            if (!results.contains(result)) {
                results.add(result);
                System.out.println(result);
            }
        }
    }

    private String play() throws InterruptedException, IOException {
        List<String> inventory = new ArrayList<>();
        // Setup computers
        IntComputer cpu = new IntComputer(code);
        // Each device runs in its own thread.
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        pool.execute(cpu);
        pool.shutdown();

        StringBuilder sb = new StringBuilder();
        StringBuilder log = new StringBuilder();
        List<String> possibleActions = new ArrayList<>();
        String latCommand = "";
        LocalTime lastCommand = LocalTime.now();
        while (true) {
            Long val = cpu.out().poll();
            if (val != null) {
                System.out.print((char) val.byteValue());
                sb.append((char) val.byteValue());
            } else {
                continue;
            }
            if (sb.toString().endsWith("Command?\n")) {
                String action = readInput();
                for (char c : action.toCharArray()) {
                    cpu.in().put((long) c);
                }
                cpu.in().put((long) '\n');

                sb = new StringBuilder();
            }

        }
    }

    Aoc25(String input) {
        this.code = input;
        results = Collections.synchronizedList(new ArrayList<>());

    }

    private String readInput() throws IOException {
        //Enter data using BufferReader
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String name = reader.readLine();

        // Printing the read line
        return name;
    }
}
