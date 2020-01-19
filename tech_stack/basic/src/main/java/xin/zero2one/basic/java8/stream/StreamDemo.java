package xin.zero2one.basic.java8.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhoujundong
 * @data 1/13/2020
 * @description Stream
 */
public class StreamDemo {

    public static void main(String[] args) {
        StreamDemo streamDemo = new StreamDemo();
        // stream 常见的 api
//        streamDemo.basic();

        // collectors 常见 api
//        streamDemo.collect();

        // stream  并发
//        streamDemo.parallel();

        // stream 短路
//        streamDemo.circuit();

        // stream 分组 分区
//        streamDemo.groupAndPartition();

    }


    public void basic() {
        Stream.of(1, 2, 3, 4).filter(e -> e % 2 == 0).map(e -> e * 2).forEach(System.out::println);
    }

    public void collect() {
        Stream.of(1, 2, 3, 4).collect(ArrayList::new, ArrayList::add, ArrayList::addAll).forEach(System.out::println);
    }

    public void parallel() {
        long nano1 = System.nanoTime();
        Stream.generate(UUID::randomUUID).limit(1000).sorted();
        long nano2 = System.nanoTime();
        Stream.generate(UUID::randomUUID).limit(1000).parallel().sorted();
        long nano3 = System.nanoTime();
        System.out.println("serial time: " + (nano2 - nano1));
        System.out.println("parallel time: " + (nano3 - nano2));
    }

    public void circuit() {
        Stream.iterate(1, a -> a + 2).limit(10).filter(a -> a < 10).forEach(System.out::println);
        Stream.iterate(1, a -> a + 2).filter(a -> a > 10).limit(10).forEach(System.out::println);
        Stream.iterate(1, a -> a + 2).filter(a -> a < 10).limit(10).forEach(System.out::println);
    }

    public void groupAndPartition() {
        StreamObject o1 = new StreamObject(1, "o1", 90);
        StreamObject o2 = new StreamObject(2, "o2", 80);
        StreamObject o3 = new StreamObject(3, "o2", 70);
        StreamObject o4 = new StreamObject(4, "o3", 100);
        HashMap<String, List<StreamObject>> collect1 = Stream.of(o1, o2, o3, o4).collect(Collectors.groupingBy(StreamObject::getName, HashMap::new, Collectors.toList()));
        System.out.println(collect1);
        Map<Boolean, List<StreamObject>> collect2 = Stream.of(o1, o2, o3, o4).collect(Collectors.partitioningBy(o -> o.getScore() >= 80, Collectors.toList()));
        System.out.println(collect2);

    }





    private class StreamObject {
        private int id;
        private String name;
        private int score;

        public StreamObject(int id, String name, int score) {
            this.id = id;
            this.name = name;
            this.score = score;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "StreamObject{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", score=" + score +
                    '}';
        }
        String s = "{\"id\":2007,\"userName\":\"zhoujundong\",\"codeCommitTimes\":\"9999\",\"testReportTimes\":\"2\",\"userRole\":\"RD\",\"realName\":\"周俊东(Education)\",\"createTime\":\"2019-04-30 10:54:18\",\"crTimes\":\"2\",\"deployTimes\":null,\"commentTimes\":null,\"joinDays\":\"262\",\"projectAccount\":\"36\",\"bugAccount\":\"0\",\"maxAccountBugQa\":\"赵婷婷\",\"maxAccountBugRd\":null,\"looked\":1}";
    }


}
