package xin.zero2one.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author zhoujundong
 * @data 12/9/2019
 * @description
 */
public class Client {

    private String host;

    private int port;

    private NioEventLoopGroup workGroup;

    private Bootstrap bootstrap;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.workGroup = new NioEventLoopGroup(1);
        bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer());
    }

    public void connect() throws InterruptedException {
        try {
            ChannelFuture connect = bootstrap.connect(host, port);
            ChannelFuture channelFuture = connect.sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client("127.0.0.1", 8888);
        client.connect();
    }

}
