package se.saidaspen.aoc2019.day22;

import se.saidaspen.aoc2019.AocUtil;
import se.saidaspen.aoc2019.Day;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;

public final class Day22 implements Day {

    public static final BigInteger TWO = BigInteger.valueOf(2);
    private final String input;
    private BigInteger shuffles;
    private BigInteger numCards;


    Day22(String input, Integer numCards) {
        this(input, BigInteger.valueOf(numCards), BigInteger.ONE);
    }

    Day22(String input, BigInteger numCards) {
        this(input, numCards, BigInteger.ONE);
    }

    Day22(String input, BigInteger numCards, BigInteger shuffles) {
        this.numCards = numCards;
        this.input = input;
        this.shuffles = shuffles;
    }

    public String part1() {
        return positionOf(BigInteger.valueOf(2019)).toString();
    }

    BigInteger positionOf(BigInteger start) {
        BigInteger result = start;
        String[] lines = input.split(System.lineSeparator());
        for (int i = 0; i < shuffles.intValue(); i++) {
            for (String line : lines) {
                if (line.startsWith("deal into new stack")) {
                    result = numCards.subtract(result).subtract(ONE);
                } else if (line.startsWith("cut")) {
                    BigInteger n = new BigInteger(line.split(" ")[1]);
                    BigInteger move = n.mod(numCards);
                    result = (result.compareTo(move) < 0) ? result.subtract(move).add(numCards) : result.subtract(move);
                } else if (line.startsWith("deal with")) {
                    BigInteger n = new BigInteger(line.replaceAll("deal with increment ", ""));
                    result = result.multiply(n).mod(numCards);
                } else if (line.length() > 0) {
                    throw new UnsupportedOperationException(line);
                }
            }
        }
        return result;
    }

    public String part2() {
        BigInteger pos = BigInteger.valueOf(2020);
        String[] lines = input.split(System.lineSeparator());
        String[] reversed = new String[lines.length];
        for (int i = 0; i < lines.length; i++) {
            reversed[i] = lines[lines.length - i - 1];
        }
        BigInteger a = ONE;
        BigInteger b = ZERO;
        for (String line : reversed) {
            if (line.startsWith("deal into new stack")) {
                a = a.negate();
                b = numCards.subtract(b).subtract(ONE);
            } else if (line.startsWith("cut")) {
                BigInteger n = new BigInteger(line.split(" ")[1]);
                b = b.add(n).mod(numCards);
            } else if (line.startsWith("deal with")) {
                BigInteger n = new BigInteger(line.replaceAll("deal with increment ", ""));
                BigInteger z = n.modInverse(numCards);
                a = a.multiply(z).mod(numCards);
                b = b.multiply(z).mod(numCards);
            } else if (line.length() > 0) {
                throw new UnsupportedOperationException(line);
            }
        }
        BigInteger[] ab = polyPow(a, b, shuffles, numCards);
        return pos.multiply(ab[0]).add(ab[1]).mod(numCards).toString();
    }

    private BigInteger[] polyPow(BigInteger a, BigInteger b, BigInteger shuffles, BigInteger numCards) {
        if (shuffles.equals(ZERO)) {
            return new BigInteger[]{ONE, ZERO}; //1,0
        } else if (shuffles.mod(TWO).equals(ZERO)) {
            return polyPow(a.multiply(a).mod(numCards), a.multiply(b).add(b).mod(numCards), shuffles.divide(TWO), numCards); // return
        } else {
            BigInteger[] cd = polyPow(a, b, shuffles.subtract(ONE), numCards);
            return new BigInteger[]{a.multiply(cd[0]).mod(numCards), a.multiply(cd[1]).add(b).mod(numCards)};
        }
    }

    public int positionOf(int i) {
        return positionOf(BigInteger.valueOf(i)).intValue();
    }
}
