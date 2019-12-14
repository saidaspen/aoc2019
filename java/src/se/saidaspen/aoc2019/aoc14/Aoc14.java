package se.saidaspen.aoc2019.aoc14;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

final class Aoc14 {

    private static Map<String, Reaction> reactions = new HashMap<>();

    public static void main(String[] args) throws Exception {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        System.out.println(new Aoc14(input).maximumFuel(1000000000000L));
    }

    Aoc14(String input) {
        Arrays.stream(input.split("\n"))
                .map(String::trim)
                .map(l -> l.split("=>"))
                .map(this::reactionOf)
                .forEach(r -> reactions.put(r.to.what, r));
    }

    long oreNeededFor(Ingredient ingredient) {
        return neededOrFor(new Ingredient(ingredient.quant, ingredient.what), new HashMap<>());
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private long neededOrFor(Ingredient ing, Map<String, Long> inv) {
        if (ing.what.equals("ORE")) {
            return ing.quant;
        } else if (inv.getOrDefault(ing.what, 0L) > ing.quant) {
            inv.put(ing.what, inv.get(ing.what) - ing.quant);
            return 0;
        }
        else {
            long needed = ing.quant - inv.getOrDefault(ing.what, 0L);
            inv.remove(ing.what);
            long oreNeeded = 0;
            Reaction reaction = reactions.get(ing.what);
            long timesReaction = (long) Math.ceil(needed *1.0 / reaction.to.quant);
            for (Ingredient reagent : reaction.from) {
                long reagentTotalQuantity = reagent.quant * timesReaction;
                oreNeeded += neededOrFor(new Ingredient(reagentTotalQuantity, reagent.what), inv);
            }
            long currInv = inv.getOrDefault(ing.what, 0L);
            inv.put(ing.what, currInv + reaction.to.quant * timesReaction - needed);
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
        long lowBound =  0;
        long mid = 0;
        while((highBound - lowBound) > 1) {
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

    private static class Reaction {
        private List<Ingredient> from;
        private Ingredient to;
        private Reaction(List<Ingredient> from, Ingredient to) {
            this.from = from;
            this.to = to;
        }
    }
}
