//package xin.zero2one.test.function;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.function.Supplier;
//import java.util.stream.Collectors;
//
///**
// * @author ZJD
// * @date 2019/4/25
// */
//public class FunctionTest {
//
//
//
//    public static void main(String[] args) {
//        mapAddTest();
//    }
//
//
//    private static void mapAddTest(){
//        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
//        Supplier<Map<Integer, Integer>> mapSupplier = () -> list.stream().collect(Collectors.toMap(k -> k, v -> v * v));
//        Map<Integer, Integer> mapAdd = list.stream().collect(Collectors.toMap(k -> k, v -> v, (v1, v2) -> v1 + v2, mapSupplier));
//        System.out.println(mapAdd);
//    }
//
//
//}
