package com.yzr.RpcPlatform.protocol;

import com.alibaba.fastjson2.JSONObject;
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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
@Slf4j
public class HttpClient {
    private static SocketChannel sc;
    public static void connect(String hostname, Integer port, Invocation invocation){
        NioEventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bs = new Bootstrap();
        try {
            bs.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpClientCodec())
                                    .addLast(new HttpObjectAggregator(65535))
                                    .addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
                                            String response = msg.content().toString(CharsetUtil.UTF_8);
                                            System.out.println("Received response:\n" + response);
                                        }
                                    });
                        }
                    });
            ChannelFuture future = bs.connect(hostname, port).sync();
            sc = (SocketChannel) future.channel();
            send(invocation);

            sc.closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }
    }

    public static void send(Invocation invocation){
//        byte[] data = HessianUtil.serialize(invocation);
//        Invocation i = HessianUtil.deserialize(data);
//        FullHttpRequest req = new DefaultFullHttpRequest(HTTP_1_1,
//                HttpMethod.GET, "/", Unpooled.copiedBuffer(data));
        FullHttpRequest req = new DefaultFullHttpRequest(HTTP_1_1,
                HttpMethod.GET, "/");
        req.headers().set(CONTENT_TYPE, "application/x-hessian");
        req.headers().set(CONTENT_LENGTH, req.content().readableBytes());
        sc.writeAndFlush(req).addListener(ChannelFutureListener.CLOSE);
    }

}
