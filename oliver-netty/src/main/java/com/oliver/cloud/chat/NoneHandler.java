package com.oliver.cloud.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @Author: Oliver
 * @Desc: 只做数据转发器
 * @Date: cretead in 2020/6/2 16:26
 * @Last Modified: by
 * @return value
 */
public class NoneHandler extends ChannelInboundHandlerAdapter {
    private Channel outChannel;

    public NoneHandler(Channel outChannel) {
        this.outChannel = outChannel;
    }

    FullHttpResponse response = null;

//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
//        System.out.println(fullHttpRequest);
//
//        FullHttpResponse response = null;
//        if (fullHttpRequest.method() == HttpMethod.GET) {
//            String data = "GET method over";
//            ByteBuf buf = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
//            response = responseOK(HttpResponseStatus.OK, buf);
//
//        } else if (fullHttpRequest.method() == HttpMethod.POST) {
//            String data = "POST method over";
//            ByteBuf content = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
//            response = responseOK(HttpResponseStatus.OK, content);
//
//        } else {
//            response = responseOK(HttpResponseStatus.INTERNAL_SERVER_ERROR, null);
//        }
//        // 发送响应
//        System.out.println("发送数据响应");
//        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("这里是服务器返回数据");
        ByteBuf in = (ByteBuf) msg;
        System.out.println("remote返回clent的报文数据" + in.toString(CharsetUtil.UTF_8));
//        报文不能再次封装
//        outChannel.writeAndFlush(msg);
//        System.out.println(outChannel+":outChannel:"+outChannel.remoteAddress());
        System.out.println("发送数据响应");
//        String data = "GET method over";
//        ByteBuf buf = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
//        response = responseOK(HttpResponseStatus.OK, buf);
//        System.out.println("组装response成功"+response);
        outChannel.writeAndFlush(in).addListener(ChannelFutureListener.CLOSE);
    }
    /**
     * @Author: Oliver
     * @Desc: response进行封装
     * @Remark: Deman
     * @Date: cretead in 2020/6/4 14:28
     * @Last Modified: by
     * @return value
     */
    private FullHttpResponse responseOK(HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        if (content != null) {
            response.headers().set("Content-Type", "text/plain;charset=UTF-8");
            response.headers().set("Content_Length", response.content().readableBytes());
        }
        return response;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        outChannel.flush();
    }
}