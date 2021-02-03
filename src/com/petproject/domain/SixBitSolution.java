package com.petproject.domain;

import java.util.BitSet;
import java.util.stream.IntStream;

public class SixBitSolution extends BitSet {

    private final int maxBits;

    public SixBitSolution(int value) {
        super(6);
        this.maxBits = 6;

        if (value < 0)
            value = +value;
        if (value > 63)
            value = 63;

        or(valueOf(new byte[]{(byte) value}));
    }

    public int getValue() {
        int accumulator = 0;
        for (int position = 0; position < maxBits; position++) {
            accumulator += (get(position) ? 1 : 0) << position;
        }
        return accumulator;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(maxBits);
        IntStream.range(0, maxBits).mapToObj(i -> get(i) ? '1' : '0').forEach(sb::append);
        return sb.reverse().toString();
    }

    public String fullInfo() {
        return String.format("{v: %2d, b: %s}", getValue(), toString());
    }
}
