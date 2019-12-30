package se.saidaspen.aoc2019.day14;

import se.saidaspen.aoc2019.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Solution to Advent of Code 2019 Day 14
 * The original puzzle can be found here: https://adventofcode.com/2019/day/14
 */
public final class Day14 implements Day {

    private static final Map<String, Reaction> REACTIONS = new HashMap<>();

    Day14(String input) {
        Arrays.stream(input.split("\n"))
                .map(String::trim)
                .map(l -> l.split("=>"))
                .map(this::reactionOf)
                .forEach(r -> REACTIONS.put(r.to.what, r));
    }

    long oreNeededFor(Ingredient ingredient) {
        return neededOrFor(new Ingredient(ingredient.quantity, ingredient.what), new HashMap<>());
    }

    private long neededOrFor(Ingredient ing, Map<String, Long> inv) {
        if (ing.what.equals("ORE")) {
            return ing.quantity;
        } else if (inv.getOrDefault(ing.what, 0L) > ing.quantity) {
            inv.put(ing.what, inv.get(ing.what) - ing.quantity);
            return 0;
        } else {
            long needed = ing.quantity - inv.getOrDefault(ing.what, 0L);
            inv.remove(ing.what);
            long oreNeeded = 0;
            Reaction reaction = REACTIONS.get(ing.what);
            long timesReaction = (long) Math.ceil(needed * 1.0 / reaction.to.quantity);
            for (Ingredient reagent : reaction.from) {
                long reagentTotalQuantity = reagent.quantity * timesReaction;
                oreNeeded += neededOrFor(new Ingredient(reagentTotalQuantity, reagent.what), inv);
            }
            long currInv = inv.getOrDefault(ing.what, 0L);
            inv.put(ing.what, currInv + reaction.to.quantity * timesReaction - needed);
            return oreNeeded;
        }
    }

    private Reaction reactionOf(String[] l) {
        List<Ingredient> from = Arrays.stream(l[0].split(",")).map(String::trim).map(i -> ingredientOf(i.split(" "))).collect(Collectors.toList());
        Ingredient to = ingredientOf(l[1].trim().split(" "));
        return new Reaction(from, to);
    }

    private Ingredient ingredientOf(String[] l) {
        return new Ingredient(Integer.parseInt(l[0]), l[1]);
    }

    long maximumFuel(Long budget) {
        long highBound = budget;
        long lowBound = 0;
        long mid = 0;
        while ((highBound - lowBound) > 1) {
            mid = (lowBound + highBound) / 2;
            long oreNeeded = oreNeededFor(new Ingredient(mid, "FUEL"));
            if (oreNeeded <= budget) {
                lowBound = mid;
            } else {
                highBound = mid;
            }
        }
        return Math.min(lowBound, mid);
    }

    @Override
    public String part1() {
        return Long.toString(oreNeededFor(new Ingredient(1, "FUEL")));
    }

    @Override
    public String part2() {
        return Long.toString(maximumFuel(1000000000000L));
    }

    private static class Reaction {
        private List<Ingredient> from;
        private Ingredient to;

        private Reaction(List<Ingredient> from, Ingredient to) {
            this.from = from;
            this.to = to;
        }
    }

    static class Ingredient {
        private long quantity;
        private String what;

        Ingredient(long quantity, String name) {
            this.quantity = quantity;
            this.what = name;
        }

        public String toString() {
            return "" + quantity + " " + what;
        }
    }
}
