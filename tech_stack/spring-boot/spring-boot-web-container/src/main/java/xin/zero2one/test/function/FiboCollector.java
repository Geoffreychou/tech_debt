//package xin.zero2one.test.function;
//
//import java.util.*;
//import java.util.function.BiConsumer;
//import java.util.function.BinaryOperator;
//import java.util.function.Function;
//import java.util.function.Supplier;
//import java.util.stream.Collector;
//
///**
// * @author ZJD
// * @date 2019/4/25
// */
//public class FiboCollector implements Collector<Integer, List<Integer>, List<Integer>> {
//    @Override
//    public Supplier<List<Integer>> supplier() {
//        return ()->{
//            List<Integer> result = new ArrayList<>();
//            result.add(0);
//            result.add(1);
//            return result;
//        };
//    }
//
//    @Override
//    public BiConsumer<List<Integer>, Integer> accumulator() {
//        return (res, num) -> {
//            res.add(res.get(res.size() - 1) + res.get(res.size() -2));
//        };
//    }
//
//    @Override
//    public BinaryOperator<List<Integer>> combiner() {
//        return null;
//    }
//
//    @Override
//    public Function<List<Integer>, List<Integer>> finisher() {
//        return (res) -> {
//            res.remove(0);
//            res.remove(1);
//            return res;
//        };
//    }
//
//    @Override
//    public Set<Characteristics> characteristics() {
//        return Collections.EMPTY_SET;
//    }
//
//    public static void main(String[] args) {
//        List<Integer> fibo = Arrays.asList(1,2,3,4,5,6,7,8,9,10).stream().collect(new FiboCollector());
//        System.out.println(fibo);
//    }
//}
