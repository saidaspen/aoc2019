package se.saidaspen.aoc2019.day22;

import lombok.Value;
import se.saidaspen.aoc2019.Day;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.math.BigInteger.*;

@Value
public final class Day22 implements Day {
    private final String input;
    private final BigInteger numCards;
    private final BigInteger shuffles;

    
    public static abstract class LinearFunction {
        abstract BigInteger k();
        abstract BigInteger m();
    }

    private static final LinearFunction ID = new LinearFunction() {
        @Override BigInteger k() { return ONE; }
        @Override BigInteger m() { return ZERO; }
    };

    /** Aggregate two functions f(x) and g(x) to create a new function h(x)=g(f(x)) */
    private LinearFunction agg(LinearFunction f, LinearFunction g) {
        // Let f(x)=k*x+m and g(x)=j*x+n, then h(x)=g(f(x))=Ax+B=j*(k*x+m)+n=j*k*x+(j*m+n) => A=j*k, B=j*m+n
        return new LinearFunction() {
            @Override BigInteger k() { return g.k().multiply(f.k()); }
            @Override BigInteger m() { return g.k().multiply(f.m()).add(g.m());}
        };
    }

    public String part1() { return positionOf(valueOf(2019)).toString(); }
    public String part2() { return cardAt(valueOf(2020)).toString(); }

    BigInteger positionOf(BigInteger in) {
        LinearFunction shuffleFunc = Arrays.stream(input.split(System.lineSeparator()))
                .filter(s -> !"".equalsIgnoreCase(s))
                .map(s -> funcOf(s, false))
                .reduce(ID, this::agg);
        BigInteger[] ab = performShuffle(shuffleFunc.k(), shuffleFunc.m(), shuffles);
        return in.multiply(ab[0]).add(ab[1]).mod(numCards);
    }

    BigInteger cardAt(BigInteger in) {
        LinearFunction shuffle = reverse(Arrays.stream(input.split(System.lineSeparator()))
                .filter(s -> !"".equalsIgnoreCase(s))
                .map(s -> funcOf(s, true))).reduce(ID, this::agg);
        BigInteger[] ab = performShuffle(shuffle.k(), shuffle.m(), shuffles);
        return in.multiply(ab[0]).add(ab[1]).mod(numCards);
    }

    public static <T> Stream<T> reverse(Stream<T> stream) {
        Iterable<T> iterable = () -> stream.collect(Collectors.toCollection(LinkedList::new)).descendingIterator();
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private LinearFunction funcOf(String s, boolean cardAt) {
        if (s.startsWith("deal into new stack")) {
            return new LinearFunction() {
                public BigInteger k() { return ONE.negate(); }
                public BigInteger m() { return ONE.negate().subtract(numCards); }
            };
        } else if (s.startsWith("cut")) {
            BigInteger n = new BigInteger(s.split(" ")[1]);
            return new LinearFunction() {
                @Override public BigInteger k() { return ONE; }
                @Override public BigInteger m() { return cardAt ? n.mod(numCards) : n.mod(numCards).negate(); }
            };
        }
        else if (s.startsWith("deal with increment")) {
            BigInteger n = new BigInteger(s.replaceAll("deal with increment ", ""));
            BigInteger z = n.modInverse(numCards);
            return new LinearFunction() {
                @Override public BigInteger k() { return cardAt ? ONE.multiply(z).mod(numCards) : n; }
                @Override public BigInteger m() { return ZERO; }
            };
        }
        throw new RuntimeException(String.format("Unsupported operation '%s'", s));
    }

    private BigInteger[] performShuffle(BigInteger a, BigInteger b, BigInteger nShuffles) {
        if (nShuffles.equals(ZERO)) {
            return new BigInteger[]{ONE, ZERO}; //1,0
        } else if (nShuffles.mod(TWO).equals(ZERO)) {
            return performShuffle(a.multiply(a).mod(numCards), a.multiply(b).add(b).mod(numCards), nShuffles.divide(TWO));
        } else {
            BigInteger[] cd = performShuffle(a, b, nShuffles.subtract(ONE));
            return new BigInteger[]{a.multiply(cd[0]).mod(numCards), a.multiply(cd[1]).add(b).mod(numCards)};
        }
    }
}
