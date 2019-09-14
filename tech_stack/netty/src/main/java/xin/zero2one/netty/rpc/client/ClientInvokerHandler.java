package xin.zero2one.netty.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhoujundong
 * @data 9/14/2019
 * @description client handler
 */
@Slf4j
public class ClientInvokerHandler extends ChannelInboundHandlerAdapter {

    private Object response;

    public Object getResponse() {
        return this.response;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.response = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        ctx.close();
    }
}
