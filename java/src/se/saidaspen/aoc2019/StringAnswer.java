package se.saidaspen.aoc2019;

import lombok.Value;

@Value(staticConstructor = "of")
public final class StringAnswer implements Answer {
    private String ans;
}
