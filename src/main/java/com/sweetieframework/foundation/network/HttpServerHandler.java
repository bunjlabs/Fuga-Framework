package com.sweetieframework.foundation.network;

import com.sweetieframework.SweetieApp;
import com.sweetieframework.foundation.Request;
import com.sweetieframework.foundation.RequestMethodUtil;
import com.sweetieframework.foundation.Response;
import com.sweetieframework.foundation.controllers.Default404NotFoundController;
import com.sweetieframework.handlers.HandlerMatched;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.stream.ChunkedStream;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: This is a most problematic place. This code must be done correctly!
class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger log = Logger.getLogger(HttpServer.class.getName());
    private final SweetieApp server;
    private HttpRequest request;
    private Response resp;

    HttpServerHandler(SweetieApp server) {
        this.server = server;

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        HttpRequest httprequest = this.request = (HttpRequest) msg;

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httprequest.getUri());
        HandlerMatched handlerMatched = server.getRouter().getMatchedByUri(queryStringDecoder.path());

        Request reuqest = new Request(RequestMethodUtil.valueOf(httprequest.getMethod().name()),
                httprequest.getUri(), ctx.channel().remoteAddress(), queryStringDecoder.parameters());

        if (handlerMatched != null) {
            resp = handlerMatched.getHandler().process(reuqest, handlerMatched.getMatches());
        } else {
            resp = new Default404NotFoundController().process(reuqest, null);
        }
        
        HttpResponse response = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1,
                ((LastHttpContent) msg).getDecoderResult().isSuccess()
                && resp != null
                ? HttpResponseStatus.valueOf(resp.getStatus()) : HttpResponseStatus.BAD_REQUEST);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, resp != null ? resp.getContentType() : "text/plain");
        if (resp.getContentLength() >= 0) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, resp.getContentLength());
        }
        if (!HttpHeaders.isKeepAlive(request)) {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        response.headers().set(HttpHeaders.Names.SERVER, "Sweetie/0.0.1.Alpha"); // how it's beautiful!

        ctx.write(response);

        ChannelFuture sendContentFuture = ctx.write(new HttpChunkedInput(
                new ChunkedStream(resp.getStream(), 8192)),
                ctx.newProgressivePromise());

        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        if (!HttpHeaders.isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.log(Level.SEVERE, "HttpServerHandler exception", cause);
        ctx.close();
    }

}
