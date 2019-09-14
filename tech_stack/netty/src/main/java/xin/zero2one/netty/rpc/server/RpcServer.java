package xin.zero2one.netty.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhoujundong
 * @data 9/14/2019
 * @description server
 */
@Slf4j
public class RpcServer {

    /**
     * port
     */
    private int port;

    public RpcServer(int port) {
        this.port = port;
    }

    public void start() {
        int processorCount = Runtime.getRuntime().availableProcessors();
        EventLoopGroup bossGroup = new NioEventLoopGroup(processorCount << 1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(processorCount << 1);
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = server.bind(port).sync();
            log.info("RPC server started, listen at port: {}", this.port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("RPC server fail to start, error msg: {}", e.getMessage(), e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new RpcServer(8888).start();
    }

}
