package se.saidaspen.aoc2019.aoc21;

import se.saidaspen.aoc2019.IntComputer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Aoc21 {

    private String input;

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        Aoc21 app = new Aoc21(input);
        app.part2();
    }

    Aoc21(String input) {
        this.input = input;
    }

    @SuppressWarnings("unused")
    private void part1() throws InterruptedException {
        String program = "NOT A J\n" +
                "NOT B T\n" +
                "OR T J\n" +
                "NOT C T\n" +
                "OR T J\n" +
                "AND D J\n" +
                "WALK\n";
        run(program);
    }

    void part2() throws InterruptedException {
        String program = "NOT A J\n" +
                "NOT B T\n" +
                "OR T J\n" +
                "NOT C T\n" +
                "OR T J\n" +
                "AND D J\n" +
                "NOT E T\n" +
                "NOT T T\n" +
                "OR H T\n" +
                "AND T J\n" +
                "RUN\n";
        run(program);
    }

    private void run(String program) throws InterruptedException {
        IntComputer cpu = new IntComputer(input);
        Thread t = new Thread(cpu);
        t.start();
        waitForCpu(cpu);
        printOutput(cpu.out());
        for (char c : program.toCharArray()) {
            cpu.send((int) c);
        }
        waitForCpu(cpu);
        printOutput(cpu.out());
    }

    private void printOutput(BlockingQueue<Long> out) throws InterruptedException {
        Long outVal = out.poll(5, TimeUnit.DAYS);
        while (outVal != null) {
            char c = (char) outVal.byteValue();
            if (isMapChar(c) || isText(c) || c == '\n')
                System.out.print(c);
            else {
                System.out.println(outVal);
            }
            outVal = out.poll(2, TimeUnit.SECONDS);
        }
    }

    private void waitForCpu(IntComputer cpu) throws InterruptedException {
        do {
            Thread.sleep(100L);
        } while (cpu.status() == IntComputer.Status.RUNNING);
    }

    private boolean isText(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ' || c == ':';
    }

    private boolean isMapChar(char c) {
        return c == '.' || c == '#' || c == '@';
    }
}
