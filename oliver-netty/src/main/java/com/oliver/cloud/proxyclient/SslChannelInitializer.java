package com.oliver.cloud.proxyclient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;
    private final boolean client;
    /**
     * @Author: Oliver
     * @Desc: 使用构造函数来传递 SSLContext 用于使用(startTls 是否启用)
     * @Date: cretead in 2020/6/1 13:58
     * @Last Modified: by
     * @return value
     */
    public SslChannelInitializer(SslContext context, boolean client, boolean startTls) {   //1
        this.context = context;
        this.startTls = startTls;
        this.client = client;
    }
    @Override
    protected void initChannel(Channel ch) throws Exception {
//        从 SslContext 获得一个新的 SslEngine 。给每个 SslHandler 实例使用一个新的 SslEngine
        SSLEngine engine = context.newEngine(ch.alloc());
//        设置是否为客户端,SslEngine 是 client 或者是 server 模式
        engine.setUseClientMode(client);
//        添加 SslHandler 到 pipeline 作为第一个处理器
        ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
        ch.pipeline().addFirst(new HttpsCodecInitializer(context,true));
        ch.pipeline().addFirst(new HttpAggregatorInitializer(true));
        ch.pipeline().addFirst(new HttpsCodecInitializer(context,true));

    }

}
