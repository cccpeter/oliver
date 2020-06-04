package com.oliver.cloud.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import org.springframework.boot.json.GsonJsonParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteResponeHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    /*
     * 处理请求
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        System.out.println(fullHttpRequest);

        FullHttpResponse response = null;
        if (fullHttpRequest.method() == HttpMethod.GET) {
            System.out.println(getGetParamsFromChannel(fullHttpRequest));
            String data = "GET method over";
            ByteBuf buf = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
            response = responseOK(HttpResponseStatus.OK, buf);

        } else if (fullHttpRequest.method() == HttpMethod.POST) {
            System.out.println(getPostParamsFromChannel(fullHttpRequest));
            String data = "POST method over";
            ByteBuf content = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
            response = responseOK(HttpResponseStatus.OK, content);

        } else {
            response = responseOK(HttpResponseStatus.INTERNAL_SERVER_ERROR, null);
        }
        // 发送响应
        System.out.println("发送数据响应");
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /*
     * 获取GET方式传递的参数
     */
    private Map<String, Object> getGetParamsFromChannel(FullHttpRequest fullHttpRequest) {

        Map<String, Object> params = new HashMap<String, Object>();

        if (fullHttpRequest.method() == HttpMethod.GET) {
            // 处理get请求
            QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
            Map<String, List<String>> paramList = decoder.parameters();
            for (Map.Entry<String, List<String>> entry : paramList.entrySet()) {
                params.put(entry.getKey(), entry.getValue().get(0));
            }
            return params;
        } else {
            return null;
        }

    }

    /*
     * 获取POST方式传递的参数
     */
    private Map<String, Object> getPostParamsFromChannel(FullHttpRequest fullHttpRequest) {

        Map<String, Object> params = new HashMap<String, Object>();

        if (fullHttpRequest.method() == HttpMethod.POST) {
            // 处理POST请求
            String strContentType = fullHttpRequest.headers().get("Content-Type").trim();
            if (strContentType.contains("x-www-form-urlencoded")) {
                params  = getFormParams(fullHttpRequest);
            } else if (strContentType.contains("application/json")) {
                try {
                    params = getJSONParams(fullHttpRequest);
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            } else {
                return null;
            }
            return params;
        } else {
            return null;
        }
    }

    /*
     * 解析from表单数据（Content-Type = x-www-form-urlencoded）
     */
    private Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();

        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }

        return params;
    }

    /*
     * 解析json数据（Content-Type = application/json）
     */
    private Map<String, Object> getJSONParams(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
        Map<String, Object> params = new HashMap<String, Object>();

        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String strContent = new String(reqContent, "UTF-8");

//        JSONObject jsonParams = JSONObject.fromObject(strContent);
        GsonJsonParser gsonJsonParser = new GsonJsonParser();
        Map<String,Object> jsonParams = gsonJsonParser.parseMap(strContent);
        for (Object key : jsonParams.keySet()) {
            params.put(key.toString(), jsonParams.get(key));
        }

        return params;
    }

    private FullHttpResponse responseOK(HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        if (content != null) {
            response.headers().set("Content-Type", "text/plain;charset=UTF-8");
            response.headers().set("Content_Length", response.content().readableBytes());
        }
        return response;
    }
//
//    //保留全局ctx
//    private ChannelHandlerContext ctx;
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
//        Channel incoming = ctx.channel();
////        缓存于全局的channelCtx
//        super.channelActive(ctx);
//        this.ctx = ctx;
//        System.out.println("proxyClient:"+incoming.remoteAddress()+"在线");
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
//        Channel incoming = ctx.channel();
//        System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"掉线");
//    }
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
//        Channel incoming = ctx.channel();
//        System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"异常");
//        // 当出现异常就关闭连接
//        cause.printStackTrace();
//        ctx.close();
//    }
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) {
//        ctx.flush();
//    }
}
