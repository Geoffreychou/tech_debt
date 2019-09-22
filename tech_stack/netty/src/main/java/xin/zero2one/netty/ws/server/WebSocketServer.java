package xin.zero2one.netty.ws.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhoujundong
 * @data 9/22/2019
 * @description ws server
 */
@Slf4j
public class WebSocketServer {

    private int port;

    public WebSocketServer(int port) {
        this.port = port;
    }


    public void start() {
        int groupNum = Runtime.getRuntime().availableProcessors() << 1;
        EventLoopGroup bossGroup = new NioEventLoopGroup(groupNum);
        EventLoopGroup workerGroup = new NioEventLoopGroup(groupNum);
        try {
            ServerBootstrap server = new ServerBootstrap();

            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketChannelHandlerInitializer());

            ChannelFuture future = server.bind(port).sync();
            log.info("WebSocket server start and listen on port: {}", port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }




    public static void main(String[] args) {
        WebSocketServer webSocketServer = new WebSocketServer(8888);
        webSocketServer.start();
    }


}
