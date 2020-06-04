package com.oliver.cloud.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;

public class HttpProxyServiceHandle extends ChannelInboundHandlerAdapter {
    private Channel clientChannel;

    public HttpProxyServiceHandle(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse response = (FullHttpResponse) msg;
        //修改http响应体返回至客户端
        response.headers().add("test","from proxy");
        System.out.println(msg);
        System.out.println("返回响应");
        clientChannel.writeAndFlush(msg);
    }
}
