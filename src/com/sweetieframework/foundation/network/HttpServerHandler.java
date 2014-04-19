/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sweetieframework.foundation.network;

import com.sweetieframework.foundation.Mapped;
import com.sweetieframework.foundation.UriHandlerBased;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import io.netty.handler.codec.http.HttpRequest;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    private HttpRequest request;
    private final StringBuilder buf = new StringBuilder();
    private Map<String, UriHandlerBased> handlers = new HashMap<>();

    public HttpServerHandler() {
        if (handlers.isEmpty()) {
            System.out.println("handlers.isEmpty()");
            /*for (Class c : ReflectionTools.getClasses("com.sweetieframework.handlers")) {
             Annotation annotation = c.getAnnotation(Mapped.class);
             if (annotation != null) {
             handlers.put(((Mapped) annotation).uri(), (UriHandlerBased) c.newInstance());
             }
             */
            try {
                Class c = ClassLoader.getSystemClassLoader().loadClass("com.sweetieframework.handlers.SomeExampleHandler");
                Annotation annotation = c.getAnnotation(Mapped.class);
                if (annotation != null) {
                    handlers.put(((Mapped) annotation).uri(), (UriHandlerBased) c.newInstance());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        UriHandlerBased handler = null;
        if (msg instanceof HttpRequest) {
            HttpRequest httprequest = this.request = (HttpRequest) msg;
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httprequest.getUri());
            buf.setLength(0);
            String context = queryStringDecoder.path();
            handler = handlers.get(context);
            if (handler != null) {
                handler.process(httprequest, buf);
            }
        }
        if (msg instanceof LastHttpContent) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1,
                    ((LastHttpContent) msg).getDecoderResult().isSuccess() ? OK : BAD_REQUEST,
                    Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8)
            );
            response.headers().set(CONTENT_TYPE, handler != null ? handler.getContentType() : "text/plain; charset=UTF-8");

            if (isKeepAlive(request)) {
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            }
            ctx.write(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
