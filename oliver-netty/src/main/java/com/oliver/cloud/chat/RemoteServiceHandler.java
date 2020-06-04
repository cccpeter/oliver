package com.oliver.cloud.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.concurrent.GlobalEventExecutor;

public class RemoteServiceHandler  extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * @Author: Oliver
     * @Desc: 获取所有已连接的socket对象
     * @Date: cretead in 2020/6/2 16:11
     * @Last Modified: by
     * @return value
     */
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
        }
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
        }
        channels.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, HttpObject msg) throws Exception { // (4)
        Channel incoming = ctx.channel();
        System.out.println("收到客户端的数据为：" + "客户端ip" + incoming.remoteAddress());
        HttpRequest req = null;
        if (msg instanceof HttpRequest) {
            req = (HttpRequest) msg;
        }
        System.out.println(req);
    }
    //保留全局ctx
    private ChannelHandlerContext ctx;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
//        缓存于全局的channelCtx
        super.channelActive(ctx);
        this.ctx = ctx;
        System.out.println("proxyClient:"+incoming.remoteAddress()+"在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"掉线");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
