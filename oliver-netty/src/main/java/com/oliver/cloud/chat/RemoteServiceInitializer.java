package com.oliver.cloud.chat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class RemoteServiceInitializer  extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("httpcode", new HttpServerCodec());
        pipeline.addLast("httpservice", new RemoteServiceHandler());


        System.out.println("SimpleChatClient:"+ch.remoteAddress() +"连接上");
    }
}
