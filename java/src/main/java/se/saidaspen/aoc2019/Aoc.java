package se.saidaspen.aoc2019;

import static java.lang.Integer.parseInt;

public class Aoc {

    public static void main(String[] args) {
        if (args.length != 2 || !args[0].matches("\\d+") || !args[1].matches("\\d")) {
            printUsage();
            System.exit(0);
        }
        int day = parseInt(args[0]);
        int part = parseInt(args[1]);
        if (day < 1 || day > 25 || part < 1 || part > 2) {
            printUsage();
            System.exit(0);
        }
        try {
            Class clazz = Aoc.class.getClassLoader().loadClass(String.format("se.saidaspen.aoc2019.day%d.Day%02d", day, day));
            Day instance = (Day) clazz.getConstructors()[0].newInstance(AocUtil.input(day));
            System.out.println(((part == 1) ? instance.part1() : instance.part2()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        System.out.print("Usage:\n" +
                "Aoc <day> <part>\n" +
                "Where day is in [1-25] and part is in [1-2]");
    }
}
