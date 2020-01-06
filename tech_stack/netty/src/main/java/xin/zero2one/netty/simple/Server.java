package xin.zero2one.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhoujundong
 * @data 12/9/2019
 * @description
 */
@Slf4j
public class Server {


    private int port;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workGroup;

    private ServerBootstrap serverBootstrap;


    public Server(int port) {
        this.port = port;
        bossGroup = new NioEventLoopGroup(8);
        workGroup = new NioEventLoopGroup(2);
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new ServerSimpleHandler())
                .childHandler(new ServerChannelInitializer());
    }

    public void start() throws InterruptedException {
        try {
            ChannelFuture sync = serverBootstrap.bind(this.port).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw e;
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(8888);
        server.start();
    }

}
