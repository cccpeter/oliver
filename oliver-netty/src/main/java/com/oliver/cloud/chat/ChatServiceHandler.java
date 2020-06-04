package com.oliver.cloud.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;

public class ChatServiceHandler extends SimpleChannelInboundHandler<HttpObject> {
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
        System.out.println("收到客户端的数据为："+"客户端ip"+incoming.remoteAddress());
//        for (Channel channel : channels) {
//            if (channel != incoming){
//                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
//            } else {
//                channel.writeAndFlush("[you]" + s + "\n");
//            }
//        }
//        转化为HttpRequest
        HttpRequest req = null;
        if (msg instanceof HttpRequest) {
            req = (HttpRequest) msg;
        }
        System.out.println(req);
//        开始向服务端转发数据，获取服务端连接
        if(req != null) {
            HttpMethod method = req.method();
//            创建http请求
            System.out.println("create request");
            Promise<Channel> promise = createPromise("localhost", 7810);
        /*
        根据是http还是http的不同，为promise添加不同的监听器
        */
            if (method.equals(HttpMethod.CONNECT)) {
                System.out.println("进入https");
                //如果是https的连接
                promise.addListener(new FutureListener<Channel>() {
                    //                @Override
                    public void operationComplete(Future<Channel> channelFuture) throws Exception {
                        //首先向浏览器发送一个200的响应，证明已经连接成功了，可以发送数据了
                        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(200, "OK"));
                        //向浏览器发送同意连接的响应，并在发送完成后移除httpcode和httpservice两个handler
                        ctx.writeAndFlush(resp).addListener(new ChannelFutureListener() {
//                            @Override
                            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                                ChannelPipeline p = ctx.pipeline();
                                p.remove("httpcode");
                                p.remove("httpservice");
                            }
                        });
                        ChannelPipeline p = ctx.pipeline();
                        //将客户端channel添加到转换数据的channel，（这个NoneHandler是自己写的）
                        //添加handler
                        p.addLast(new NoneHandler(channelFuture.getNow()));
                    }
                });
            } else {
                //如果是http连接，首先将接受的请求转换成原始字节数据
                System.out.println("转化原生字节流");
                EmbeddedChannel em = new EmbeddedChannel(new HttpRequestEncoder());
                em.writeOutbound(req);
                System.out.println("http转化原始字节流" + em.toString());
                final Object o = em.readOutbound();
                em.close();
                promise.addListener(new FutureListener<Channel>() {
//                   @Override
                    public void operationComplete(Future<Channel> channelFuture) throws Exception {
                        //移除	httpcode	httpservice 并添加	NoneHandler，并向服务器发送请求的byte数据
                        ChannelPipeline p = ctx.pipeline();
                        p.remove("httpcode");
                        p.remove("httpservice");
                        //添加handler
                        p.addLast(new NoneHandler(channelFuture.getNow()));
                        channelFuture.get().writeAndFlush(o);
                    }
                });
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
//        缓存于全局的channelCtx
        super.channelActive(ctx);
        this.ctx = ctx;
        System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"在线");
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
    /**
     * @Author: Oliver
     * @Desc: 创建与远程服务器的连接
     * @Date: cretead in 2020/6/2 16:30
     * @Last Modified: by
     * @return value
     */
    //保留全局ctx
    private ChannelHandlerContext ctx;
    //创建一会用于连接web服务器的	Bootstrap
    private Bootstrap b = new Bootstrap();

    private Promise<Channel> createPromise(String host, int port) {
        final Promise<Channel> promise = ctx.executor().newPromise();

        b.group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .handler(new NoneHandler(ctx.channel()))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .connect()
                .addListener(new ChannelFutureListener() {
//                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            promise.setSuccess(channelFuture.channel());
                        } else {
                            ctx.close();
                            channelFuture.cancel(true);
                        }
                    }
                });
        return promise;
    }
}
