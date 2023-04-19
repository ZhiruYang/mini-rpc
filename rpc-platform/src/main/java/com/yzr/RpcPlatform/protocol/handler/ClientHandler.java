package com.yzr.RpcPlatform.protocol.handler;

import com.alibaba.fastjson2.JSONObject;
import com.yzr.RpcCommon.userService;
import com.yzr.RpcPlatform.common.Invocation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        String response = msg.content().toString(CharsetUtil.UTF_8);
        System.out.println("Received response:\n" + response);
    }

}
