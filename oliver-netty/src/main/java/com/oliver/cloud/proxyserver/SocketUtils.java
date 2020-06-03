package com.oliver.cloud.proxyserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class SocketUtils {

    public static void sendHello(ChannelHandlerContext ctx, String from, boolean isSecure){
        if( null != ctx){
            send(ctx, Constants.CODE_HELLO_MSG + from + System.getProperty("line.separator"));
        }
    }

    public static void sendLineBaseText(ChannelHandlerContext ctx, String text){
        if( null != ctx){
            send(ctx, text + System.getProperty("line.separator"));
        }
    }

    /**
     * @Author: Oliver
     * @Desc: desc
     * @Date: cretead in 2020/6/2 9:29
     * @Last Modified: by
     * @return value
     */
    private static void send(ChannelHandlerContext ctx, String log){
        if( null != ctx && null != log){
            ByteBuf msgBuf = Unpooled.buffer(log.length());
            msgBuf.writeBytes(log.getBytes(StandardCharsets.UTF_8));
            ctx.writeAndFlush(msgBuf);
        }
    }

}
