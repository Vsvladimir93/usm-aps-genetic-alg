package com.petproject.domain;

import com.petproject.util.Util;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.petproject.util.Util.getRandom;

public class SolutionGenerator {

    private SolutionGenerator() {
    }

    public static List<SixBitSolution> generate(int population, int upperBound) {
        if (population <= 0 || upperBound <= 0) {
            throw new IllegalArgumentException("Population and UpperBound must be greater than 0.");
        }

        return generateSolutions(population, upperBound);
    }

    public static List<SixBitSolution> requestManualInputValues(int population, int upperBound) {
        if (population <= 0 || upperBound <= 0) {
            throw new IllegalArgumentException("Population and UpperBound must be greater than 0.");
        }

        Scanner s = new Scanner(System.in);

        return Stream.generate(() -> {
            System.out.println("Введите значение для следующего \"Решения\":");
            return getNextSolution(s, upperBound);
        })
                .limit(population)
                .collect(Collectors.toList());
    }

    private static SixBitSolution getNextSolution(Scanner s, int upperBound) {
        return new SixBitSolution(Util.getNextInt(0, upperBound, s));
    }

    private static List<SixBitSolution> generateSolutions(int population, int upperBound) {
        return Stream.generate(() -> getRandom().nextInt(upperBound))
                .limit(population)
                .map(SixBitSolution::new)
                .collect(Collectors.toList());
    }

}
