package com.oliver.cloud.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("开始与服务端交换数据");
        outChannel.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        outChannel.flush();
    }
}