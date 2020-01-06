package xin.zero2one.netty.custom.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import xin.zero2one.netty.custom.handler.ServerChildHandlerInitializer;

/**
 * @author zhoujundong
 * @data 12/16/2019
 * @description TODO
 */
public class CustomServer {


    private int port;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workGroup;

    private ServerBootstrap serverBootstrap;

    public CustomServer(int port) {
        this.port = port;
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerChildHandlerInitializer());
    }


    public void start() throws InterruptedException {
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        new CustomServer(8888).start();
    }

}
