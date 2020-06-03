package com.oliver.cloud.study;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: Oliver
 * @Desc: 抛弃的服务端，也就是只收不回
 * @Date: cretead in 2020/5/20 17:50
 * @Last Modified: by
 * @return value
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // (2)  默默地丢弃收到的数据
//        try {
//            // Do something with msg
//            ((ByteBuf) msg).release();
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }

//        低效的循环
//        ByteBuf in = (ByteBuf) msg;
//        try {
//            while (in.isReadable()) { // (1)
//                System.out.print((char) in.readByte());
//                System.out.flush();
//            }
//        } finally {
//            ReferenceCountUtil.release(msg); // (2)
//        }

//        打印
//        ByteBuf in = (ByteBuf) msg;
//        System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
//        进行应答
        ctx.write(msg); // (1)
        ByteBuf in = (ByteBuf) msg;
        System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
        ctx.flush(); // (2)

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

}
