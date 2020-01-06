package xin.zero2one.netty.custom.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import xin.zero2one.netty.custom.model.CustomModel;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author zhoujundong
 * @data 12/16/2019
 * @description
 */
@Slf4j
public class CustomDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        CharSequence charSequence = in.readCharSequence(in.readableBytes(), Charset.forName("UTF-8"));
        CustomModel customModel = JSONObject.parseObject(charSequence.toString(), CustomModel.class);
        log.info("msg: {}", JSON.toJSONString(customModel));
//        ctx.fireChannelRead(customModel);

        out.add(customModel);
//        ctx.fireChannelRead(out);
    }
}
