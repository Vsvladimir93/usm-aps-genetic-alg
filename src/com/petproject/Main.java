package com.petproject;

import com.petproject.domain.SixBitSolution;
import com.petproject.domain.GeneticAlgorithm;
import com.petproject.domain.SolutionGenerator;
import com.petproject.util.Util;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static int population;
    private static int generations;
    private static int solutionUpperBound = 64;
    private static List<SixBitSolution> solutions;

    public static void main(String[] args) {
        // Инициализация всех необходимых параметров ГА через консоль
        // requestData();

        // Тестовый набор всех необходимых параметров ГА
        testData();


        start();
    }

    private static void requestData() {
        Scanner s = new Scanner(System.in);

        System.out.println("Введите число популяции: ");

        population = Util.getNextInt(1, Integer.MAX_VALUE, s);

        System.out.println("Введите максимальное число поколений: ");

        generations = Util.getNextInt(1, Integer.MAX_VALUE, s);

        System.out.printf("Верхний предел изначального значения \"Решения\": %d\n", solutionUpperBound);

        solutions = SolutionGenerator.generate(population, solutionUpperBound);
    }

    private static void testData() {
        population = 4;
        generations = 4;
        solutions = SolutionGenerator.generate(population, solutionUpperBound);
        // Util.enableLog(false);
    }


    private static void start() {
        GeneticAlgorithm alg = new GeneticAlgorithm(solutions);
        alg.run();
    }


}
