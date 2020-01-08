package xin.zero2one.basic.java8.lambda;

/**
 * @author zhoujundong
 * @data 1/8/2020
 * @description lambda
 */
public class LambdaDemo {

    public static void main(String[] args) {
        LambdaInterface lambdaInterface = () -> System.out.println("it is lambda expression");
        lambdaInterface.print();
        lambdaInterface.printDefault();
    }

    @FunctionalInterface
    public interface LambdaInterface {

        void print();

        default void printDefault() {
            System.out.println("print default");
        }
    }

}
