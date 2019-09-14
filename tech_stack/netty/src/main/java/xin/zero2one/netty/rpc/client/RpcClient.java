package xin.zero2one.netty.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import xin.zero2one.netty.rpc.client.proxy.RpcProxy;

import java.lang.reflect.Proxy;

/**
 * @author zhoujundong
 * @data 9/14/2019
 * @description client
 */
@Slf4j
public class RpcClient {

    private String ip;
    private int port;

    public RpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public <T> T getRemoteInstance(Class<T> clazz) {
        if (!clazz.isInterface()) {
            throw new RuntimeException("clazz is not interface");
        }
        RpcProxy rpcProxy = new RpcProxy(clazz, this);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, rpcProxy);
    }


    public Object invoke(Object msg) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        ClientInvokerHandler clientInvokerHandler = new ClientInvokerHandler();
        try {
            Bootstrap client = new Bootstrap();
            client.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientChannelInitializer(clientInvokerHandler));
            ChannelFuture channelFuture = client.connect(this.ip, this.port).sync();
            channelFuture.channel().writeAndFlush(msg).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("invoke remote error" , e);
            throw new RuntimeException(e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
        return clientInvokerHandler.getResponse();
    }


}
