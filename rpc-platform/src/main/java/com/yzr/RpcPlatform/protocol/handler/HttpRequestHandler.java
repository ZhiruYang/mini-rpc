package com.yzr.RpcPlatform.protocol.handler;

import com.yzr.RpcCommon.userService;
import com.yzr.RpcPlatform.common.HessianUtil;
import com.yzr.RpcPlatform.common.Invocation;
import com.yzr.RpcPlatform.register.LocalRegister;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        ByteBuf buf = request.content();
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);

        // 处理请求
        Invocation inv = (Invocation) HessianUtil.deserialize(data);

        Class clazz = LocalRegister.get(inv.getInterfaceName());
        Method method = clazz.getMethod(inv.getMethodName(), inv.getParameterTypes());
        Constructor constructor = clazz.getConstructor();

        String ret = (String) method.invoke(constructor.newInstance(), inv.getParameters());
        log.info(ret);

        // 回复
        ByteBuf content = Unpooled.copiedBuffer(ret.getBytes());
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
