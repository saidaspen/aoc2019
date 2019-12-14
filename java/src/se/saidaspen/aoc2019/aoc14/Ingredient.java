package se.saidaspen.aoc2019.aoc14;


public class Ingredient {
    long quant;
    String what;

    Ingredient(long quant, String s) {
        this.quant =  quant;
        this.what = s;
    }

    public String toString() {
        return "" + quant + " " + what;
    }
}
