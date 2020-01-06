package xin.zero2one.nio.buffer;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author zhoujundong
 * @data 10/1/2019
 * @description TODO
 */
@Slf4j
public class ByteBufferDemo {

    public static void main(String[] args) {
        ByteBufferDemo byteBufferDemo = new ByteBufferDemo();
        byteBufferDemo.test();
    }


    public void test() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        baseInfo(byteBuffer);
        byteBuffer.put("test buffer".getBytes());
        baseInfo(byteBuffer);
        byteBuffer.flip();
        baseInfo(byteBuffer);




    }


    private void baseInfo(ByteBuffer byteBuffer) {
        log.info("buffer: position: {}, limit: {}, capacity: {}, mark: {}, isDirect: {}",
                byteBuffer.position(), byteBuffer.limit(), byteBuffer.capacity(), byteBuffer.mark(), byteBuffer.isDirect());
    }

}
