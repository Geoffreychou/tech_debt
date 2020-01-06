package xin.zero2one.nio.channel;

import lombok.extern.slf4j.Slf4j;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author zhoujundong
 * @data 10/1/2019
 * @description TODO
 */
@Slf4j
public class FileChannelDemo {

    public static void main(String[] args) throws Exception {
        FileChannel fileChannel = FileChannel.open(Paths.get("from"), StandardOpenOption.READ, StandardOpenOption.WRITE);
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());
        mappedByteBuffer.clear();
        mappedByteBuffer.put("aaaa".getBytes());
//        mappedByteBuffer.put("tttttttt".getBytes());



    }


}
