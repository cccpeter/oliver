package com.oliver.cloud.proxyclient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @Author: Oliver
 * @Desc: 添加https的支持
 * @Date: cretead in 2020/6/1 14:07
 * @Last Modified: by
 * @return value
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {

    private final SslContext context;
    private final boolean client;

    public HttpsCodecInitializer(SslContext context, boolean client) {
        this.context = context;
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine engine = context.newEngine(ch.alloc());
//        添加 SslHandler 到 pipeline 来启用 HTTPS
        pipeline.addFirst("ssl", new SslHandler(engine));  //1

        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());  //2
        } else {
            pipeline.addLast("codec", new HttpServerCodec());  //3
        }
    }
}
