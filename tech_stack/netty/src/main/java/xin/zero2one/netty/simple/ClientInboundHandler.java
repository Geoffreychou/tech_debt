package xin.zero2one.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author zhoujundong
 * @data 12/9/2019
 * @description
 */
@Slf4j
public class ClientInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("test".getBytes()));
        log.info("channelActive", ctx.channel());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int length = byteBuf.readableBytes();
        CharSequence charSequence = byteBuf.readCharSequence(length, Charset.forName("UTF-8"));
        log.info(charSequence.toString());
    }
}
