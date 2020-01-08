package xin.zero2one.basic.java8.function;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author zhoujundong
 * @data 1/8/2020
 * @description BiFunction
 */
public class BiFunctionDemo {

    public static void main(String[] args) {
        BiFunction<String,Long,Integer> biFunction =
                (arg1, arg2) -> Integer.parseInt(arg1) + arg2.intValue();
        Integer r1 = biFunction.apply("1", 2L);
        System.out.println(r1);


        Function<Integer,String> afterFunction = (arg1) -> String.valueOf(arg1);

        String r2 = biFunction.andThen(afterFunction).apply("1", 2L);
        System.out.println(r2);

    }

}
