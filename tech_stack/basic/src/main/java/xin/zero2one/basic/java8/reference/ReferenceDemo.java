package xin.zero2one.basic.java8.reference;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author zhoujundong
 * @data 1/8/2020
 * @description method reference & constructor reference & instance reference
 */
public class ReferenceDemo {

    public ReferenceDemo() {
        System.out.println(this);
    }

    public void print() {
        System.out.println("print: " + this);
    }

    public void print(ReferenceDemo referenceDemo) {
        System.out.println("print reference: " + referenceDemo);
    }

    public static void printStatic(ReferenceDemo referenceDemo) {
        System.out.println("printStatic reference: " + referenceDemo);
    }

    public void consume(Consumer<ReferenceDemo> consumer) {
        consumer.accept(this);
    }

    public static void consume(Supplier<ReferenceDemo> supplier, Consumer<ReferenceDemo> consumer) {
        consumer.accept(supplier.get());
    }

    public static void main(String[] args) {
        ReferenceDemo referenceDemo = new ReferenceDemo();
        referenceDemo.consume(referenceDemo::print);
        referenceDemo.consume(ReferenceDemo::print);

        ReferenceDemo.consume(ReferenceDemo::new, referenceDemo::print);

        ReferenceDemo.consume(ReferenceDemo::new, ReferenceDemo::printStatic);

        ReferenceDemo.consume(ReferenceDemo::new, ReferenceDemo::print);

    }

}
