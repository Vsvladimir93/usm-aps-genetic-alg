package com.petproject.domain;

import java.util.*;

import static com.petproject.util.Util.print;

public class GeneticAlgorithm {

    private final List<SixBitSolution> solutions;

    public GeneticAlgorithm(List<SixBitSolution> solutions) {
        this.solutions = solutions;
    }

    public void run() {
        // Шаг 1 - визуализация решений
        print("\nНачальная популяция \"Решений\":");
        solutions.forEach(s -> print("%s ", s.fullInfo()));

        // Шаг 2 - Случайная выборка пар решений
        print("\nПервая выборка случайных пар \"Решений\":");
        randomParentsSelection(solutions).forEach((key, value) -> print(
                "Пара: %s | %s",
                key.fullInfo(),
                value.fullInfo()
        ));
    }

    private Map<SixBitSolution, SixBitSolution> randomParentsSelection(List<SixBitSolution> solutions) {
        Map<SixBitSolution, SixBitSolution> parents = new HashMap<>(solutions.size());

        // Создаем Stack решений
        Stack<SixBitSolution> randomSelection = new Stack<>();
        randomSelection.addAll(solutions);
        // Перемешиваем коллекцию
        Collections.shuffle(randomSelection);

        // Забираем из перемешанной коллекции элементы поочередно, и создаем пары.
        while (randomSelection.size() >= 2) {
            parents.put(randomSelection.pop(), randomSelection.pop());
        }

        return parents;
    }

}
