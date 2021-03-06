package xin.zero2one.basic.java8.function;

import java.util.function.Supplier;

/**
 * @author zhoujundong
 * @data 1/8/2020
 * @description Supplier
 */
public class SupplierDemo {

    public static void main(String[] args) {
        Supplier<Integer> supplier = () -> 1;
        Integer r = supplier.get();
        System.out.println(r);
    }

}
