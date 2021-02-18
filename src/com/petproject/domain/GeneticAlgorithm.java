package com.petproject.domain;

import com.petproject.util.Util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.petproject.util.Util.getRandom;
import static com.petproject.util.Util.print;

public class GeneticAlgorithm {

    private final int mutationChance;
    private final int populationQuantity;
    private final int maxGenerations;
    private final int qualityUpperBound;

    private final Function<BitSolution, Integer> function;

    private final List<BitSolution> solutions;

    private int currentGeneration;

    public GeneticAlgorithm(List<BitSolution> solutions, Function<Integer, Integer> function, int mutationChance, int populationQuantity, int maxGenerations, int qualityUpperBound) {
        this.solutions = solutions;
        this.mutationChance = mutationChance;
        this.populationQuantity = populationQuantity;
        this.maxGenerations = maxGenerations;
        this.qualityUpperBound = qualityUpperBound == 0 ? Integer.MAX_VALUE : qualityUpperBound;
        this.function = s -> function.apply(s.getValue());
    }

    public void run() {
        currentGeneration = 0;

        List<BitSolution> result = cycle(solutions);

        print("\nПоколение: %d", currentGeneration);
        print("\nИтоговая популяция:");
        result.forEach(s -> print(s.fullInfo()));

        Optional<BitSolution> best = findBest(result);
        if (best.isPresent()) {
            int bestQuality = best.map(function).orElse(0);
            print("\nЛучшее Решение: %s - f(x) -> %d", best.get().fullInfo(), bestQuality);
        }
    }

    private List<BitSolution> cycle(List<BitSolution> solutions) {
        ++currentGeneration;

        // Шаг 1 - Визуализация начальных решений
        print("\nПопуляция \"Решений\" номер: %d", currentGeneration);
        solutions.forEach(s -> print("%s ", s.fullInfo()));

        // Шаг 2 - Случайная выборка пар решений
        List<Pair<BitSolution>> randomPairs = selection(solutions);
        print("\nВыборка случайных пар \"Решений\":");
        randomPairs.forEach((pair) -> print(
                "Пара: %s | %s",
                pair.getFirst().fullInfo(),
                pair.getSecond().fullInfo()
        ));

        // Шаг 3 - Скрещевание пар
        List<BitSolution> crossedSolutions = crossing(randomPairs);
        print("\nСкрещенные потомки:");
        crossedSolutions.forEach((child) -> print(child.fullInfo()));

        // Шаг 4 - Мутация новых решений
        print("\nМутация новых \"Решений\":");
        List<BitSolution> mutated = mutation(crossedSolutions);
        mutated.forEach(s -> print("%s ", s.fullInfo()));

        // Шаг 5 - Расширение популяции за счет новых решений
        print("\nРасширенная популяция за счет новых \"Решений\":");
        List<BitSolution> expanded = expansion(mutated, solutions);
        expanded.forEach(s -> print("%s ", s.fullInfo()));

        // Шаг 6 - Сокращение расширенной популяции до исходного размера
        print("\nСокращенная популяция до исходных размеров \"Решений\":");
        List<BitSolution> reduced = reduction(expanded);
        reduced.forEach(s -> print("%s ", s.fullInfo()));

        // Шаг 6.1 - Качество популяции
        int populationQuality = qualityAll(reduced);
        print("\nКачество популяции: %d (среднее значение)", populationQuality);

        Optional<BitSolution> best = findBest(reduced);
        int bestQuality = best.map(function).orElse(0);
        print("Лучшее Решение: %s", bestQuality);

        List<String> qualityEach = calculateQualityForEach(reduced);
        qualityEach.forEach(Util::print);

        // Шаг 7 - Проверка - Хоть один из критериев завершения выполняется ?
        boolean isCompleteResult = isComplete(reduced, bestQuality);
        print("\nПроверка - Хоть один из критериев завершения выполняется? - %s", isCompleteResult  ? "Да" : "Нет");

        // Если выполняется проверка - выход из рекурсии. Иначе решения проходят следующий цикл.
        return isCompleteResult ? reduced : cycle(reduced);
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
        boolean shouldMutate = (getRandom().nextInt(100) + 1) <= mutationChance;

        if (shouldMutate) {
            // Находит случайное Решение
            BitSolution s = solutions.get(getRandom().nextInt(solutions.size()));
            // Инвертирует случайный бит
            s.flip(getRandom().nextInt(s.getMaxBits()));
        }

        return solutions;
    }

    /**
     * Расширение популяции за счет новых решений
     */
    private List<BitSolution> expansion(List<BitSolution> newSolutions, List<BitSolution> originSolutions) {
        originSolutions.addAll(newSolutions);
        return originSolutions;
    }

    /**
     * Сокращение расширенной популяции до исходного размера
     */
    private List<BitSolution> reduction(List<BitSolution> expandedSolutions) {
        // Сортировка решений в нисходящем порядке по результату вычисления функции
        Comparator<BitSolution> compare = (a, b) -> function.apply(b) - function.apply(a);
        expandedSolutions.sort(compare);

        // Выборка первых
        return expandedSolutions.stream().limit(populationQuantity).collect(Collectors.toList());
    }

    /**
     * Качество популяции
     */
    private int qualityAll(List<BitSolution> reducedSolutions) {
        return reducedSolutions.stream().map(function).reduce(Integer::sum).orElse(0) / populationQuantity;
    }

    /**
     * Качество каждой особи
     */
    private List<String> calculateQualityForEach(List<BitSolution> reducedSolutions) {
        Function<BitSolution, String> calculate = s -> s.fullInfo()
                .concat(" - f(x) -> " + function.apply(s));
        return reducedSolutions.stream().map(calculate).collect(Collectors.toList());

    }

    /**
     * Проверка - Хоть один из критериев завершения выполняется ?
     */
    private boolean isComplete(List<BitSolution> reduced, int bestQuality) {
        if (currentGeneration >= maxGenerations) {
            return true;
        } else if (bestQuality != 0 && bestQuality >= qualityUpperBound) {
            return true;

            // Возвращает true если все элементы коллекции одинаковые
        } else return reduced.isEmpty()
                || reduced.stream()
                .map(BitSolution::getValue)
                .allMatch(v -> v == reduced.get(0).getValue());
    }

    private Optional<BitSolution> findBest(List<BitSolution> reducedSolutions) {
        Comparator<BitSolution> compare = (a, b) -> function.apply(b) - function.apply(a);
        reducedSolutions.sort(compare);
        return reducedSolutions.stream().findFirst();
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
        first.setPart(pair.getFirst().getPart(0, randomDelimiter), 0, randomDelimiter);
        first.setPart(
                pair.getSecond().getPart(randomDelimiter, pair.getSecond().getMaxBits()),
                randomDelimiter,
                pair.getSecond().getMaxBits()
        );

        randomDelimiter = getRandom().nextInt(pair.getFirst().getMaxBits() - 2) + 1;

        BitSolution second = new BitSolution(pair.getFirst().getMaxBits());
        // Создание второго потомка с помощью операции or
        // из второй части первого предка
        // и первой части второго предка
        second.setPart(
                pair.getFirst().getPart(randomDelimiter, pair.getFirst().getMaxBits()),
                randomDelimiter,
                pair.getFirst().getMaxBits()
        );
        second.setPart(pair.getSecond().getPart(0, randomDelimiter), 0, randomDelimiter);

        return new Pair<>(first, second);
    }


}
