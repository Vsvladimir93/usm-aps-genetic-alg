package com.petproject;

import com.petproject.domain.BitSolution;
import com.petproject.domain.GeneticAlgorithm;
import com.petproject.domain.SolutionGenerator;

import java.util.List;
import java.util.function.Function;

public class Main {

    private static final int maxBits = 6;

    private static int populationSize;
    private static int mutationChance;
    private static int maxGenerations;
    private static int qualityUpperBound = 0;

    private static final Function<Integer, Long> function = x -> -1L * (x - 8) * (x - 18) * (x - 30) * (x - 58);

    private static List<BitSolution> solutions;

    public static void main(String[] args) {
        initData();
        start();
    }

    private static void start() {
        GeneticAlgorithm ga = new GeneticAlgorithm(
                solutions,
                function,
                mutationChance,
                populationSize,
                maxGenerations,
                qualityUpperBound
        );
        ga.run();
    }

    private static void initData() {
        if (maxBits < 3)
            throw new IllegalArgumentException("maxBits must be greater or equals 3 !");

        populationSize = 10;
        maxGenerations = 40;
        mutationChance = 20;
        qualityUpperBound = 0;
        solutions = SolutionGenerator.generate(populationSize, maxBits);
    }

}
