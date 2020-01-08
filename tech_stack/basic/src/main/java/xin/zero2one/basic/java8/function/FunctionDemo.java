package xin.zero2one.basic.java8.function;

import java.util.function.Function;

/**
 * @author zhoujundong
 * @data 1/8/2020
 * @description function
 */
public class FunctionDemo {

    public static void main(String[] args) {
        Function<String, Integer> function = arg -> Integer.parseInt(arg) * 2;
        Integer r1 = function.apply("1");
        System.out.println(r1);

        Function<Integer, String> functionBefore = arg -> String.valueOf(arg + 1);

        Function<Integer, Integer> functionAfter = arg -> arg - 1;

        Integer r2 = function.compose(functionBefore).apply(1);
        System.out.println(r2);

        Integer r3 = function.andThen(functionAfter).apply("1");
        System.out.println(r3);
    }
}
