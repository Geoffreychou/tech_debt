package xin.zero2one.basic.java8.function;

import java.util.function.Predicate;

/**
 * @author zhoujundong
 * @data 1/8/2020
 * @description predicate
 */
public class PredicateDemo {

    public static void main(String[] args) {
        Predicate<Integer> predicate = arg -> arg > 0;
        boolean r1 = predicate.test(1);
        System.out.println(r1);

        Predicate<Integer> andPredicate = arg -> arg > 100;
        boolean r2 = predicate.and(andPredicate).test(5);
        System.out.println(r2);


        Predicate<Integer> orPredicate = arg -> arg < 0;
        boolean r3 = predicate.or(orPredicate).test(0);
        System.out.println(r3);

        boolean r4 = predicate.negate().test(1);
        System.out.println(r4);

        Object obj = new Object();
        boolean r5 = Predicate.isEqual(obj).test(obj);
        System.out.println(r5);

    }

}
