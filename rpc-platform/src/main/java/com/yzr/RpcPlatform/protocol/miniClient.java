package com.yzr.RpcPlatform.protocol;

import com.yzr.RpcCommon.userService;
import com.yzr.RpcPlatform.common.HessianUtil;
import com.yzr.RpcPlatform.common.Invocation;
import com.yzr.RpcPlatform.protocol.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class miniClient {
    private EventLoopGroup group;
    private Bootstrap bootstrap;

    public miniClient() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
                        pipeline.addLast(new ClientHandler());
                    }
                });
    }

    public void sendRequest(String url) {
        URI uri = URI.create(url);
        String host = uri.getHost();
        int port = uri.getPort() == -1 ? 80 : uri.getPort();
        String path = uri.getRawPath();
        if (path == null || path.length() == 0) {
            path = "/";
        }

        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            future.channel().writeAndFlush(request).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
    public void send(Invocation invocation){
        byte[] data = HessianUtil.serialize(invocation);
        Invocation i = HessianUtil.deserialize(data);
        FullHttpRequest req = new DefaultFullHttpRequest(HTTP_1_1,
                HttpMethod.GET, "/", Unpooled.copiedBuffer(data));
//        FullHttpRequest req = new DefaultFullHttpRequest(HTTP_1_1,
//                HttpMethod.GET, "/");
        req.headers().set(CONTENT_TYPE, "application/x-hessian");
        req.headers().set(CONTENT_LENGTH, req.content().readableBytes());

        try {
            ChannelFuture future = bootstrap.connect("localhost", 9999).sync();
            future.channel().writeAndFlush(req).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

//    public static void main(String[] args) {
//        miniClient client = new miniClient();
////        client.sendRequest("http://localhost:9999");
//
//        Invocation invocation = new Invocation(userService.class.getName(),
//                "Hi", new Class[]{String.class}, new Object[]{"ddr"});
//        client.send(invocation);
//    }
}

