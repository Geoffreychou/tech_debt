package xin.zero2one.netty.custom.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;
import xin.zero2one.netty.custom.handler.ClientHandlerInitializer;
import xin.zero2one.netty.custom.model.CustomModel;

/**
 * @author zhoujundong
 * @data 12/17/2019
 * @description
 */
public class CustomClient {

    private String host;

    private int port;

    private EventLoopGroup group;

    private Bootstrap bootstrap;

    public CustomClient(String host, int port) {
        this.host = host;
        this.port = port;
        group = new NioEventLoopGroup(1);
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandlerInitializer())
                .remoteAddress(host, port);

    }

    public void send(CustomModel msg) throws InterruptedException {
        try {
            ChannelFuture channelFuture = bootstrap.connect().sync();
            ByteBuf buffer = Unpooled.buffer();
            byte[] bytes = JSON.toJSONBytes(msg);
            buffer.writeInt(bytes.length);
            buffer.writeBytes(bytes);
            channelFuture.channel().writeAndFlush(buffer).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CustomClient customClient = new CustomClient("127.0.0.1", 8888);
        CustomModel customModel = new CustomModel();
        customModel.setCode(0);
        customModel.setMsg("client");
        customModel.setData("hello, server");
        customClient.send(customModel);
    }
}
