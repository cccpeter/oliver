package com.oliver.cloud.chat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class SimpleChatServerInitializer extends
        ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

//        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//        进入handler前需要进行解码器解码，并指定正确
//        string的解码器
//        pipeline.addLast("decoder", new StringDecoder());
//        pipeline.addLast("encoder", new StringEncoder());
//        pipeline.addLast("handler", new ChatServiceHandler());
//        http的解码器
        pipeline.addLast("httpcode", new HttpServerCodec());
        pipeline.addLast("httpservice", new ChatServiceHandler());


        System.out.println("SimpleChatClient:"+ch.remoteAddress() +"连接上");
    }
}
