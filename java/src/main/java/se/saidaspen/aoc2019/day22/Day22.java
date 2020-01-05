package se.saidaspen.aoc2019.day22;

import lombok.Value;
import se.saidaspen.aoc2019.Day;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.System.lineSeparator;
import static java.math.BigInteger.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toCollection;

/**
 * Solution to Advent of Code 2019 Day 22
 * The original puzzle can be found here: https://adventofcode.com/2019/day/22
 * */
@Value public final class Day22 implements Day {

    private static final String ERR_OP = "Unsupported shuffle operation '%s'";

    private static final String DEAL_INTO = "deal into new stack";
    private static final String CUT = "cut";
    private static final String DEAL_W_INC = "deal with increment";

    private final String input;
    private final BigInteger nCards, nShuffles;

    /** Represents any linear function that can be written f(x)=kx+m */
    @Value public static class LinFunc {
        BigInteger k, m;
        BigInteger apply(BigInteger x) { return x.multiply(k).add(m); }
    }

    /* The identity function: f(x)=x */
    private static final LinFunc ID = new LinFunc(ONE, ZERO);

    /** Aggregate two functions f(x) and g(x) to create a new function h(x)=g(f(x)) */
    private LinFunc agg(LinFunc f, LinFunc g) {
        // Let f(x)=k*x+m and g(x)=j*x+n, then h(x) = g(f(x)) = Ax+B = j*(k*x+m)+n = j*k*x + (j*m+n) => A=j*k, B=j*m+n
        return new LinFunc(g.k.multiply(f.k), g.k.multiply(f.m).add(g.m));
    }

    public String part1() { return positionOf(valueOf(2019)).toString(); }
    public String part2() { return cardAt(valueOf(2020)).toString(); }

    BigInteger positionOf(BigInteger in) {
        LinFunc shuffle = stream(input.split(lineSeparator()))
                .filter(s -> !"".equalsIgnoreCase(s))
                .map(s -> {
                    if (s.startsWith(DEAL_INTO))        return new LinFunc(ONE.negate(), ONE.negate());
                    else if (s.startsWith(CUT))         return new LinFunc(ONE, argOf(s).mod(nCards).negate());
                    else if (s.startsWith(DEAL_W_INC))  return new LinFunc(argOf(s), ZERO);
                    throw new RuntimeException(String.format(ERR_OP, s));
                })
                .reduce(ID, this::agg); // Create one func of all shuffle operations, i.e. like: f(x)=f1((f2(f3(x)))
        return executeTimes(shuffle.k, shuffle.m, nShuffles).apply(in).mod(nCards);
    }

    BigInteger cardAt(BigInteger in) {
        LinFunc shuffle = reverse(stream(input.split(lineSeparator()))
                .filter(s -> !"".equalsIgnoreCase(s))
                .map(s -> {
                    if (s.startsWith(DEAL_INTO))        return new LinFunc(ONE.negate(), ONE.negate().subtract(nCards));
                    else if (s.startsWith(CUT))         return new LinFunc(ONE, argOf(s).mod(nCards));
                    else if (s.startsWith(DEAL_W_INC))  {
                        BigInteger z = argOf(s).modInverse(nCards);
                        return new LinFunc(ONE.multiply(z).mod(nCards), ZERO);
                    }
                    throw new RuntimeException(String.format(ERR_OP, s));
                }))
                .reduce(ID, this::agg); // Create one func of all shuffle operations, i.e. like: f(x)=f1((f2(f3(x)))
        return executeTimes(shuffle.k, shuffle.m, nShuffles).apply(in).mod(nCards);
    }

    /** Strips everything out of a string except for a number and then creates a BigInteger out of it */
    private BigInteger argOf(String s) { return new BigInteger(s.replaceAll("[^-?0-9]+", "")); }

    public static <T> Stream<T> reverse(Stream<T> stream) {
        Iterable<T> iterable = () -> stream.collect(toCollection(LinkedList::new)).descendingIterator();
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private LinFunc executeTimes(BigInteger k, BigInteger m, BigInteger nShuffles) {
        if (nShuffles.equals(ZERO)) {
            return ID;
        } else if (nShuffles.mod(TWO).equals(ZERO)) {
            return executeTimes(k.multiply(k).mod(nCards), k.multiply(m).add(m).mod(nCards), nShuffles.divide(TWO));
        } else {
            LinFunc cd = executeTimes(k, m, nShuffles.subtract(ONE));
            return new LinFunc(k.multiply(cd.k).mod(nCards), k.multiply(cd.m).add(m).mod(nCards));
        }
    }
}
