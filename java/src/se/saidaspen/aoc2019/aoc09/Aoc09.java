package se.saidaspen.aoc2019.aoc09;

import se.saidaspen.aoc2019.IntComputer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

public class Aoc09 {

    public static void main(String[] args) throws IOException, InterruptedException {
        Long[] code = Arrays.stream(new String(Files.readAllBytes(Paths.get(args[0]))).split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        new Aoc09().run(code, 1L); // 2 for Part 2, 1 for Part 1
    }

    private void run(Long[] code, Long i) throws InterruptedException {
        ArrayBlockingQueue<Long> in = new ArrayBlockingQueue<>(10000);
        ArrayBlockingQueue<Long> out = new ArrayBlockingQueue<>(10000);
        in.put(i);
        IntComputer amp = new IntComputer(code, in, out);
        amp.run();
        System.out.println(out.poll());
    }
}
