package xin.zero2one.netty.custom.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import xin.zero2one.netty.custom.model.CustomModel;

/**
 * @author zhoujundong
 * @data 12/17/2019
 * @description
 */
@Slf4j
public class ClientInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("ServerInboundHandler msg: {}", msg);
//        CustomModel resp = new CustomModel();
//        resp.setCode(200);
//        resp.setMsg("success");
//        resp.setData(1);
//
//        byte[] bytes = JSON.toJSONBytes(resp);
//
//        ByteBuf buffer = Unpooled.buffer();
//        buffer.writeInt(bytes.length);
//        buffer.writeBytes(bytes);
//        ctx.writeAndFlush(buffer);

        ReferenceCountUtil.release(msg);
//        ctx.close();

    }

}
