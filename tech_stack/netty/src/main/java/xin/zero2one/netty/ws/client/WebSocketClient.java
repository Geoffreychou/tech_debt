package xin.zero2one.netty.ws.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author zhoujundong
 * @data 9/22/2019
 * @description ws client
 */
@Slf4j
public class WebSocketClient {

    private int port;

    private String host;

    private URI uri;

    public WebSocketClient(String url) throws URISyntaxException {
        URI uri = new URI(url);
        this.uri = uri;
        this.host = uri.getHost();
        this.port = uri.getPort();
    }

    public void send() throws IOException, InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);

        try {
            Bootstrap client = new Bootstrap();

            client.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new WebSocketClientChannelHandlerInitializer(uri));

            Channel channel = client.connect(host, port).sync().channel();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = reader.readLine();
                if (msg == null) {
                    break;
                } else if ("bye".equals(msg.toLowerCase())) {
                    channel.writeAndFlush(new CloseWebSocketFrame());
                    channel.closeFuture().sync();
                    break;
                } else if ("ping".equals(msg.toLowerCase())) {
                    WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
                    channel.writeAndFlush(frame);
                } else {
                    WebSocketFrame frame = new TextWebSocketFrame(msg);
                    channel.writeAndFlush(frame);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }


    public static void main(String[] args) throws Exception {
        String url = "ws://127.0.0.1:8888/ws";
        WebSocketClient webSocketClient = new WebSocketClient(url);
        webSocketClient.send();
    }

}
