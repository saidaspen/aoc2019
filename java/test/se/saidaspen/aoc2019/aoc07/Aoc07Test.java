package se.saidaspen.aoc2019.aoc07;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Aoc07Test {

    @Test
    public void test_part1_1(){
        Long[] code = Arrays.stream("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0".split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        System.out.println(Aoc07.findLargestThrust(code, List.of(4L,3L,2L,1L,0L)));
    }

}
