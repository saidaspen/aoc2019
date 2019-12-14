package se.saidaspen.aoc2019.aoc14;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

final class Aoc14 {


    public static void main(String[] args) throws Exception {
        String input = new String(Files.readAllBytes(Paths.get(args[0])));
        new Aoc14(input).part1();
    }


    Map<String, Reaction> reactions = new HashMap<>();

    private Aoc14(String input) {
        Arrays.stream(input.split("\n"))
                .map(String::trim)
                .map(l -> l.split("=>"))
                .map(this::reactionOf)
                .forEach(r -> reactions.put(r.to.what, r));

        List<Ingredient> needed = new ArrayList<>(whatsNeededFor(new Ingredient(1, "FUEL")));
        while (!onlyOre(needed))  {
            List<Ingredient> neededNew = new ArrayList<>();
            for (Ingredient i : needed) {
                if (i.what.equalsIgnoreCase("ORE")) {
                    neededNew.add(i);
                } else {
                    neededNew.addAll(whatsNeededFor(i));
                }
            }
            needed = neededNew;
        }

        double oreNeeded = needed.stream().mapToDouble(i -> i.quant).sum();
        System.out.println("oreNeeded: " + oreNeeded);

    }

    private List<Ingredient> whatsNeededFor(Ingredient f) {
        ArrayList<Ingredient> needed = new ArrayList<>();
        Reaction r = reactions.get(f.what);
        for (Ingredient i : r.from) {
            double howMuch = Math.max(i.quant / r.to.quant, i.quant);
            needed.add(new Ingredient(howMuch, i.what));
        }
        return needed;
    }

    private boolean onlyOre(List<Ingredient> needed) {
        for (Ingredient i : needed) {
            if (!i.what.equalsIgnoreCase("ORE")) {
                return false;
            }
        }
        return true;
    }

    private Reaction reactionOf(String[] l) {
        List<Ingredient> from = Arrays.stream(l[0].split(",")).map(String::trim).map(i -> ingredientOf(i.split(" "))).collect(Collectors.toList());
        Ingredient to = ingredientOf(l[1].trim().split(" "));
        return new Reaction(from, to);
    }

    private Ingredient ingredientOf(String[] l) {
        return new Ingredient(Integer.parseInt(l[0]), l[1]);
    }

    private void part1() throws Exception {
    }

    private class Reaction {
        List<Ingredient> from = new ArrayList<>();
        Ingredient to;

        public Reaction(List<Ingredient> from, Ingredient to) {
            this.from = from;
            this.to = to;
        }
    }
    private class Ingredient {
        double quant;
        String what;

        public Ingredient(double quant, String s) {
            this.quant =  quant;
            this.what = s;
        }
    }
}
