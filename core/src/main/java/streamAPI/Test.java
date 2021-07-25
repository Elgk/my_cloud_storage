package streamAPI;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Test {

    void func(String res) {

    }

    void func(Supplier<String> res) {

    }

    public static void main(String[] args) {
        Func f = new Func() {
            @Override
            public int func(int a, int b) {
                return a % b;
            }
        };
        int ff = f.func(33, 7);
        Func f2 = new Func() {
            @Override
            public int func(int a, int b) {
                return a * 2 + b;
            }
        };
        Func f3 = (a, b) -> 10 * a + b;
        System.out.println("get: " + f3.func(2, 11));
        System.out.println("result: " + f2.func(3, 5));
        int xx = Integer.sum(1, 23);
        Func func = Integer::sum; //  передана ссылка на уже существующую функцию Integer.sum(), она подходит по сигнатуре интерейса
        System.out.println(func.func(1, 2));

        System.out.println(func.getClass());

        // ________ основные интерфейсы__________________
        // Consumer, Function, Prdicate, Supplier
        // использование:
        // Consumer  -- peek, forEach
        // Predicate  -- filter
        // Function  --  map, flatMap отображение элементов
        // Supplier --  Collect поставщик коллекций
        // на практике используются выражения в правой части рассмотренных конструкций


        Consumer<String> consumer = System.out::println;
        Consumer<String> con = s -> s = s + 1 + 2;


        // Predicate  -- filter
        Predicate<Integer> predicate = x -> x > 10;
        System.out.println(predicate.test(12));
        // Function  --  map, flatMap
        Function<String, Integer> st = str -> str.length();

        System.out.println("длина: " + st.apply("dsjakdhak"));
        // преобразует тиа  А в тип В
        // пример
        Function<String, Integer> intMapper = String::length;
        // Supplier --  Collect поставщик коллекций
        Supplier<ArrayList<String>> listSupplier = ArrayList::new;
        // то же самое
        Supplier<ArrayList<String>> list = () -> new ArrayList<>();

        // _______    примеры задач        // _______
        // числа, делящиеся на 3
        IntStream.rangeClosed(1, 10)
                .boxed()
                .filter(x -> x % 3 == 0)
                .forEach(System.out::println);
        // числа, сумма цифр которых > 8 (выделить из двузначного числа цифры и сложить)
        IntStream.rangeClosed(10, 35)
                .boxed()
                .filter(x -> x / 10 + x % 10 > 8)
                .forEach(System.out::println);
        System.out.println("res: " + (5 / 3) + " " + 8 % 3);
        //увеличить каждый элемент на 2
        IntStream.rangeClosed(1, 5)
                .boxed()
                .map(x -> x + 2)
                .forEach(System.out::println);
        System.out.println("res: " + (5 / 3) + " " + 8 % 3);

        //вычислить сумму всех элементов
        Integer red = IntStream.rangeClosed(1, 5)
                .boxed()
                .map(x -> x + 2)
                .reduce(0, Integer::sum);
        System.out.println("summa: " + red);
        //вычислить произведение всех элементов
        Integer mult = IntStream.rangeClosed(1, 5)
                .boxed()
                .reduce(1, ((left, right) -> left * right));   // identity - это аккумулятор, накапливает результат операции
        System.out.println("mult : " + mult);

       
    }
}