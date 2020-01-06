package xin.zero2one.netty.simple;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @author zhoujundong
 * @data 12/9/2019
 * @description
 */
public class ClientChannelInitializer extends ChannelInitializer<Channel> {


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ClientInboundHandler());
    }
}
