package com.petproject.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.petproject.util.Util.getRandom;

public class SolutionGenerator {

    private SolutionGenerator() {
    }

    public static List<BitSolution> generate(int population, int upperBound, int maxBits) {
        if (population <= 0 || upperBound <= 0) {
            throw new IllegalArgumentException("Population and UpperBound must be greater than 0.");
        }

        return generateSolutions(population, upperBound, maxBits);
    }

    private static List<BitSolution> generateSolutions(int population, int upperBound, int maxBits) {
        return Stream.generate(() -> getRandom().nextInt(upperBound))
                .limit(population)
                .map(randomValue -> new BitSolution(maxBits, randomValue))
                .collect(Collectors.toList());
    }

}
