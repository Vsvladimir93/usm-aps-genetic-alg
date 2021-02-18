package com.petproject;

import com.petproject.domain.BitSolution;
import com.petproject.domain.GeneticAlgorithm;
import com.petproject.domain.SolutionGenerator;

import java.util.List;
import java.util.function.Function;

public class Main {

    private static final int maxBits = 6;
    private static final int solutionUpperBound = 1 << maxBits;

    private static int population;
    private static int mutationChance;
    private static int maxGenerations;
    private static int qualityUpperBound = 0; // максимум 217341

    private static final Function<Integer, Integer> function = x -> -1 * (x - 8) * (x - 18) * (x - 30) * (x - 58);

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
                population,
                maxGenerations,
                qualityUpperBound
        );
        ga.run();
    }

    private static void initData() {
        population = 10;
        maxGenerations = 40;
        mutationChance = 2;
        qualityUpperBound = 0;
        solutions = SolutionGenerator.generate(population, solutionUpperBound, maxBits);
    }

}
