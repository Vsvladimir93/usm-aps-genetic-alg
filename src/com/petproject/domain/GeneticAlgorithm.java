package com.petproject.domain;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.petproject.util.Util.getRandom;
import static com.petproject.util.Util.print;

public class GeneticAlgorithm {

    private final int mutationChance;
    private final int population;
    private final List<BitSolution> solutions;

    public GeneticAlgorithm(List<BitSolution> solutions, int mutationChance, int population) {
        this.solutions = solutions;
        this.mutationChance = mutationChance;
        this.population = population;
    }

    public void run() {

        // Шаг 1 - Визуализация начальных решений
        print("\nНачальная популяция \"Решений\":");
        solutions.forEach(s -> print("%s ", s.fullInfo()));

        // Шаг 2 - Случайная выборка пар решений
        List<Pair<BitSolution>> randomPairs = selection(solutions);
        print("\nПервая выборка случайных пар \"Решений\":");
        randomPairs.forEach((pair) -> print(
                "Пара: %s | %s",
                pair.getFirst().fullInfo(),
                pair.getSecond().fullInfo()
        ));

        // Шаг 3 - Скрещевание пар
        List<BitSolution> crossedSolutions = crossing(randomPairs);
        print("\nСкрещенные потомки:");
        crossedSolutions.forEach((child) -> print(child.fullInfo()));

        // Шаг 4 - Мутация новых Решений
        print("\nМутация новых \"Решений\":");
        List<BitSolution> mutated = mutation(crossedSolutions);
        mutated.forEach(s -> print("%s ", s.fullInfo()));

        // Шаг 5 - Расширение популяции за счет новых решений
        print("\nРасширенная популяция за счет новых \"Решений\":");
        List<BitSolution> expanded = expansion(mutated);
        expanded.forEach(s -> print("%s ", s.fullInfo()));

        // Шаг 6 - Сокращение расширенной популяции до исходного размера
        print("\nСокращенная популяция до исходных размеров \"Решений\":");
        List<BitSolution> reduced = reduction(expanded);
        reduced.forEach(s -> print("%s ", s.fullInfo()));

        // Шаг 6.1 - Качество популяции
        int populationQuality = quality(reduced);
        print("\nКачество популяции: %d", populationQuality);
    }

    /**
     * Выборка случайных пар
     */
    private List<Pair<BitSolution>> selection(List<BitSolution> solutions) {
        // Перемешиваем решения и возвращаем новую коллекцию
        Stack<BitSolution> shuffled = shuffledStack(solutions);
        List<Pair<BitSolution>> pairs = new ArrayList<>();

        // Забираем из перемешанной коллекции элементы поочередно, и создаем пары.
        while (shuffled.size() >= 2) {
            pairs.add(new Pair<>(shuffled.pop(), shuffled.pop()));
        }

        return pairs;
    }

    /**
     * Скрещевание пар
     */
    private List<BitSolution> crossing(List<Pair<BitSolution>> pairs) {
        return pairs.stream()
                .map(this::crossPair)
                .flatMap(p -> Stream.of(p.getFirst(), p.getSecond()))
                .collect(Collectors.toList());
    }

    /**
     * Мутация решений
     */
    private List<BitSolution> mutation(List<BitSolution> solutions) {
        // Высчитывает шанс мутации
        Predicate<BitSolution> shouldMutate = s -> (getRandom().nextInt(100) + 1) <= mutationChance;
        // Инвертирует случайный бит
        Function<BitSolution, BitSolution> mutate = s -> {
            s.flip(getRandom().nextInt(s.getMaxBits()));
            return s;
        };

        return solutions.stream()
                .map(s -> shouldMutate.test(s) ? mutate.apply(s) : s)
                .collect(Collectors.toList());
    }

    /**
     * Расширение популяции за счет новых решений
     */
    private List<BitSolution> expansion(List<BitSolution> newSolutions) {
        solutions.addAll(newSolutions);
        return solutions;
    }

    /**
     * Сокращение расширенной популяции до исходного размера
     */
    private List<BitSolution> reduction(List<BitSolution> expandedSolutions) {
        // Сортировка решений в нисходящем порядке по значению
        Collections.sort(expandedSolutions);
        // Выборка первых
        return expandedSolutions.stream().limit(population).collect(Collectors.toList());
    }

    /**
     * Качество популяции
     */
    private int quality(List<BitSolution> reducedSolutions) {
        return reducedSolutions.stream().map(BitSolution::getValue).reduce(Integer::sum).orElse(0);
    }

    private Stack<BitSolution> shuffledStack(List<BitSolution> solutions) {
        Stack<BitSolution> shuffled = new Stack<>();
        shuffled.addAll(solutions);

        Collections.shuffle(shuffled);
        return shuffled;
    }


    private Pair<BitSolution> crossPair(Pair<BitSolution> pair) {
        // Для каждой пары берем случайный делитель от 1 до (maxBits - 1)
        int randomDelimiter = getRandom().nextInt(pair.getFirst().getMaxBits() - 2) + 1;

        BitSolution first = new BitSolution(pair.getFirst().getMaxBits());
        // Создание первого потомка с помощью операции or
        // из первой части первого предка
        // и второй части второго предка
        first.or(pair.getFirst().getPart(0, randomDelimiter));
        first.or(pair.getSecond().getPart(randomDelimiter, pair.getSecond().getMaxBits()));

        randomDelimiter = getRandom().nextInt(pair.getFirst().getMaxBits() - 2) + 1;

        BitSolution second = new BitSolution(pair.getFirst().getMaxBits());
        // Создание второго потомка с помощью операции or
        // из второй части первого предка
        // и первой части второго предка
        second.or(pair.getFirst().getPart(randomDelimiter, pair.getFirst().getMaxBits()));
        second.or(pair.getSecond().getPart(0, randomDelimiter));

        return new Pair<>(first, second);
    }


}
