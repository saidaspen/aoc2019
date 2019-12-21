package se.saidaspen.aoc2019.aoc21;

import se.saidaspen.aoc2019.IntComputer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class Aoc21 {

    private final String input;

    public static void main(String[] args) throws IOException, InterruptedException {
        Aoc21 app = new Aoc21(new String(Files.readAllBytes(Paths.get(args[0]))));
        app.part2();
    }

    Aoc21(String input) {
        this.input = input;
    }

    @SuppressWarnings("unused")
    private void part1() throws InterruptedException {
        run("NOT A J\n" +
            "NOT B T\n" +
            "OR T J\n" +
            "NOT C T\n" +
            "OR T J\n" +
            "AND D J\n" +
            "WALK\n");
    }

    void part2() throws InterruptedException {
        run("NOT A J\n" +
            "NOT B T\n" +
            "OR T J\n" +
            "NOT C T\n" +
            "OR T J\n" +
            "AND D J\n" +
            "NOT E T\n" +
            "NOT T T\n" +
            "OR H T\n" +
            "AND T J\n" +
            "RUN\n");
    }

    private void run(String program) throws InterruptedException {
        IntComputer cpu = new IntComputer(input);
        new Thread(cpu).start();
        waitFor(cpu);
        print(cpu.out());
        send(cpu, program);
        waitFor(cpu);
        print(cpu.out());
    }

    private void send(IntComputer cpu, String program) throws InterruptedException {
        for (char c : program.toCharArray()) {
            cpu.send((int) c);
        }
    }

    private void print(BlockingQueue<Long> out) throws InterruptedException {
        Long outVal = out.poll(5, TimeUnit.SECONDS);
        while (outVal != null) {
            char c = (char) outVal.byteValue();
            if (isMapChar(c) || isText(c))
                System.out.print(c);
            else {
                System.out.println(outVal);
            }
            outVal = out.poll(2, TimeUnit.SECONDS);
        }
    }

    private void waitFor(IntComputer cpu) throws InterruptedException {
        do {
            Thread.sleep(100L);
        } while (cpu.status() == IntComputer.Status.RUNNING);
    }

    private boolean isText(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ' || c == ':' || c == '\n';
    }

    private boolean isMapChar(char c) {
        return c == '.' || c == '#' || c == '@';
    }
}
