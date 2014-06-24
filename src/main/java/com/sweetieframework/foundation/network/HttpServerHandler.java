package com.sweetieframework.foundation.network;

import com.sweetieframework.SweetieServer;
import com.sweetieframework.handlers.Handler;
import com.sweetieframework.handlers.Mapped;
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
import java.util.logging.Level;
import java.util.logging.Logger;

class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = Logger.getLogger(HttpServer.class.getName());
    private HttpRequest request;
    private final StringBuilder buf = new StringBuilder();
    private SweetieServer server;

    HttpServerHandler(SweetieServer server) {
        this.server = server;

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Handler handler = null;
        if (msg instanceof HttpRequest) {
            HttpRequest httprequest = this.request = (HttpRequest) msg;
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httprequest.getUri());
            buf.setLength(0);
            String context = queryStringDecoder.path();
            handler = server.getHandlersMap().getMatchedByUri(context);
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
        log.log(Level.SEVERE, "HttpServerHandler exception", cause);
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Nothing
    }

}
