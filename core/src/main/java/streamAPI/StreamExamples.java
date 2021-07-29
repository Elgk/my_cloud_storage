package streamAPI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamExamples {

    public static void main(String[] args) throws URISyntaxException, IOException {
        URI digits = StreamExamples.class.getResource("digits.txt").toURI();
        URI digits2 = StreamExamples.class.getResource("digits2.txt").toURI();
        URI text = StreamExamples.class.getResource("text.txt").toURI();

        Files.lines(Paths.get(digits))
                .flatMap(str -> Arrays.stream(str.split(" +")) )// преобразует строку  в массив
              //  .map(str -> Integer.parseInt(str))  // применяем функцию parseInt к каждому элементу стрима (переводит символ в число)
                // или то же самое
                .map(Integer::parseInt)
                .filter(p-> p % 2 == 0)
                .forEach(System.out::println);

        Integer res = Files.lines(Paths.get(digits))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .map(Integer::parseInt)
                .filter(x -> x % 2 == 0)
                .reduce(0, Integer::sum);

        System.out.println(res);  // сумма всех элементов

        // map - подготовка данных к обработке
        // reduce - рассчет

        // x + y + identity = x + y
        // x * y * identity = x * y

        // 1 - 4
       Map<Integer, Integer> integerMap = Files.lines(Paths.get(digits2))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .map(Integer::parseInt)
                .collect(Collectors.toMap(
                        Function.identity(),
                        x -> 1,
                        Integer::sum
                ));
        System.out.println(integerMap);

        List<Integer> list1 = Files.lines(Paths.get(digits2))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .map(Integer::parseInt)
                .distinct()
                .collect(Collectors.toList());

        System.out.println(list1);

        Map<Integer, Integer> integerMapR = Files.lines(Paths.get(digits2))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .map(Integer::parseInt)
                .reduce(
                        new HashMap<>(),
                        (x, y) -> {
                            x.put(y, x.getOrDefault(y, 0) + 1);
                            return x;
                        },
                        (x, y) -> {
                            x.putAll(y);
                            return x;
                        });

        System.out.println(integerMapR);

        // 1 - 1+1+1+1
        // 2 - 2+2+2+2
        // группировка и рассчет суммы  элементов в группе
        Map<Integer, Integer> sumMap = Files.lines(Paths.get(digits2))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .map(Integer::parseInt)
                .collect(Collectors.toMap(
                        Function.identity(), // ключ  - элемент стрима 1, 2, 3...
                        Function.identity(), // база
                        Integer::sum
                ));
        // 1 1 2 2 3
        // 1 - 1
        // 1 - 1 + 1
        // 2 - 2 + 2
        System.out.println(sumMap);

        // 1 - [1,1,1,1]
        // 2 - [2,2]

        // 1 - [1]

        Map<Integer, List<Integer>> digitListMap = Files.lines(Paths.get(digits2))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .map(Integer::parseInt)
                .collect(Collectors.toMap(
                        Function.identity(),
                        x -> {
                            List<Integer> list = new ArrayList<>();
                            list.add(x);
                            return list;
                        },
                        (left, right) -> {
                            left.addAll(right);
                            return left;
                        }
                ));

        System.out.println(digitListMap);


        Map<String, Integer> wordsMap = Files.lines(Paths.get(text))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .filter(word -> word != null && !word.isEmpty())
                .map(String::toLowerCase)
                .map(word -> word.replaceAll("'s", ""))
                .map(word -> word.replaceAll("\\W+", ""))
                .filter(word -> word.matches("[a-z]+"))
                .collect(Collectors.toMap(
                        Function.identity(), // ключ для map
                        val -> 1,            // значение
                        Integer::sum
                ));

        System.out.println(wordsMap);
        wordsMap.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }
}
