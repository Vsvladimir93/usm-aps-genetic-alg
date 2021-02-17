package com.petproject;

import com.petproject.domain.BitSolution;
import com.petproject.domain.GeneticAlgorithm;
import com.petproject.domain.SolutionGenerator;
import com.petproject.util.Util;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static int maxBits = 6;
    private static int population;
    private static int generations;
    private static int mutationChance;
    private static int solutionUpperBound = 64;
    private static List<BitSolution> solutions;


    public static void main(String[] args) {
        // Инициализация всех необходимых параметров ГА через консоль
        // requestData(maxBits);

//         Тестовый набор всех необходимых параметров ГА
        testData(maxBits);
        start();

    }

    private static void requestData(int maxBits) {
        Scanner s = new Scanner(System.in);

        System.out.println("Введите число популяции: ");

        population = Util.getNextInt(1, Integer.MAX_VALUE, s);

        System.out.println("Введите максимальное число поколений: ");

        generations = Util.getNextInt(1, Integer.MAX_VALUE, s);

        System.out.println("Введите шанс мутации от 0 до 100: ");

        mutationChance = Util.getNextInt(0, 100, s);

        System.out.printf("Верхний предел изначального значения \"Решения\": %d\n", solutionUpperBound);

        solutions = SolutionGenerator.generate(population, solutionUpperBound, maxBits);
    }

    private static void testData(int maxBits) {
        population = 4;
        generations = 4;
        mutationChance = 3;
        solutions = SolutionGenerator.generate(population, solutionUpperBound, maxBits);
    }


    private static void start() {
        GeneticAlgorithm ga = new GeneticAlgorithm(solutions, mutationChance);
        ga.run();
    }


}
