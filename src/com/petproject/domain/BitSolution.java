package com.petproject.domain;

import java.util.BitSet;
import java.util.stream.IntStream;

public class BitSolution extends BitSet {

    private final int maxBits;

    public BitSolution(int maxBits, int value) {
        super(maxBits);
        this.maxBits = maxBits;

        if (value < 0 || value > 1 << maxBits) {
            throw new IllegalArgumentException("Value greater than maxBits size");
        }

        or(valueOf(new long[]{(long) value}));
    }

    public BitSolution(int maxBits) {
        super(maxBits);
        this.maxBits = maxBits;
    }

    public int getMaxBits() {
        return maxBits;
    }

    public int getValue() {
        int accumulator = 0;
        for (int position = 0; position < maxBits; position++) {
            accumulator += (get(position) ? 1 : 0) << position;
        }
        return accumulator;
    }

    public BitSolution getPart(int fromIndexInclusive, int toIndexExclusive) {
        if ((fromIndexInclusive < 0 || fromIndexInclusive > maxBits)
                || (toIndexExclusive < 0 || toIndexExclusive > maxBits))
            throw new IllegalArgumentException("indexes must be in range from 0 to " + (maxBits - 1));

        if (fromIndexInclusive > toIndexExclusive)
            throw new IllegalArgumentException("fromIndexInclusive must be less than toIndexExclusive");

        BitSolution part = new BitSolution(maxBits);
        for (int position = fromIndexInclusive; position < toIndexExclusive; position++) {
            if (get(position)) {
                part.set(position);
            }
        }

        return part;
    }

    public void setPart(BitSolution part, int fromIndexInclusive, int toIndexExclusive) {
        if ((fromIndexInclusive < 0 || fromIndexInclusive > maxBits)
                || (toIndexExclusive < 0 || toIndexExclusive > maxBits))
            throw new IllegalArgumentException("indexes must be in range from 0 to " + (maxBits - 1));

        if (fromIndexInclusive > toIndexExclusive)
            throw new IllegalArgumentException("fromIndexInclusive must be less than toIndexExclusive");

        for (int position = fromIndexInclusive; position < toIndexExclusive; position++) {
            set(position, part.get(position));
        }
    }

    public String fullInfo() {
        return String.format("{v: %2d, b: %s}", getValue(), toString());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(maxBits);
        IntStream.range(0, maxBits).mapToObj(i -> get(i) ? '1' : '0').forEach(sb::append);
        return sb.reverse().toString();
    }

}
