package xin.zero2one.netty.ws.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;

/**
 * @author zhoujundong
 * @data 9/22/2019
 * @description
 */
public class WebSocketClientChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private URI uri;

    public WebSocketClientChannelHandlerInitializer(URI uri) {
        this.uri = uri;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(2048));
        pipeline.addLast(new WebSocketClientProtocolHandler(
                WebSocketClientHandshakerFactory
                        .newHandshaker(uri, WebSocketVersion.V13, null, false,
                                new DefaultHttpHeaders()), true));
        pipeline.addLast(new WebSocketClientResponseHandler());


    }
}
