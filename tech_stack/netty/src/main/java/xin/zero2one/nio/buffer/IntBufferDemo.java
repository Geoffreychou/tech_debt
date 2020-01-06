package xin.zero2one.nio.buffer;

import java.nio.IntBuffer;

/**
 * @author zhoujundong
 * @data 9/23/2019
 * @description TODO
 */
public class IntBufferDemo {


    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);
        buffer.put(1);
        System.out.println(buffer.position());
    }

}
