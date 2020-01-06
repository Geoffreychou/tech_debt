package xin.zero2one.nio.server;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhoujundong
 * @data 9/29/2019
 * @description  server by nio
 */
@Slf4j
public class Server {

    private int port;

    public Server(int port) {
        this.port = port;
    }


    public void start() {

        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                try {
                    selector.select();

                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey selectionKey = it.next();
                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel socketChannel = channel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        } else if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            socketChannel.configureBlocking(false);
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            socketChannel.read(byteBuffer);
                            byteBuffer.flip();
                            log.info("send from client: {}", new String(byteBuffer.array()));
                            socketChannel.register(selector, SelectionKey.OP_WRITE);
                        } else if (selectionKey.isWritable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            socketChannel.configureBlocking(false);
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            byteBuffer.put("hello client".getBytes());
                            byteBuffer.flip();
                            socketChannel.write(byteBuffer);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }
                        it.remove();
                    }
                } catch (Exception e) {
                    log.error("Server error ", e);
                }
            }
        } catch (Exception e) {
            log.error("Server error ", e);
        }
    }

    public static void main(String[] args) {
        new Server(8888).start();
    }


}
