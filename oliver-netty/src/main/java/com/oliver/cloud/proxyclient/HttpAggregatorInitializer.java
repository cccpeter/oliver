package com.oliver.cloud.proxyclient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpAggregatorInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
//            客户端进行加密
            pipeline.addLast("codec", new HttpClientCodec());
//            client: 添加 HttpContentDecompressor 用于处理来自服务器的压缩的内容
            pipeline.addLast("decompressor",new HttpContentDecompressor());
        } else {
//            服务端进行加密
            pipeline.addLast("codec", new HttpServerCodec());
//            server: HttpContentCompressor 用于压缩来自 client 支持的 HttpContentCompressor
            pipeline.addLast("compressor",new HttpContentCompressor());
        }
        pipeline.addLast("aggegator", new HttpObjectAggregator(512 * 1024));
    }
}
