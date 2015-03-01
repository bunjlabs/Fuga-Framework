package com.showvars.sweetie.network;

import com.showvars.sweetie.SweetieApp;
import com.showvars.sweetie.foundation.Context;
import com.showvars.sweetie.foundation.Cookies;
import com.showvars.sweetie.foundation.Request;
import com.showvars.sweetie.foundation.RequestMethod;
import com.showvars.sweetie.foundation.Response;
import com.showvars.sweetie.sessions.Session;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.stream.ChunkedStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final Logger log = Logger.getLogger(HttpServer.class.getName());
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
    private final SweetieApp app;
    private HttpRequest httprequest;
    private Request request;
    private Response resp;
    private HttpPostRequestDecoder decoder;
    private Collection<Cookie> cookiesUpload;
    private String lastSessionId = "";

    HttpServerHandler(SweetieApp app) {
        this.app = app;

    }

    private void reset() {
        httprequest = null;
        request = null;
        resp = null;
        decoder = null;
        cookiesUpload = null;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpRequest) {
            this.httprequest = (HttpRequest) msg;

            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httprequest.getUri());

            request = new Request(
                    RequestMethod.valueOf(httprequest.getMethod().name()),
                    httprequest.headers().get("HOST"),
                    httprequest.getUri(), queryStringDecoder.path(),
                    ctx.channel().remoteAddress());

            request.setParameters(queryStringDecoder.parameters());

            Cookies cookiesDownload = new Cookies();
            cookiesUpload = new ArrayList<>();

            String cookieString = httprequest.headers().get(HttpHeaders.Names.COOKIE);
            List<String> sessions = new ArrayList<>();
            if (cookieString != null) {
                for (Cookie cookie : CookieDecoder.decode(cookieString)) {
                    cookiesDownload.put(cookie.getName(), cookie);
                }
            }
            request.setCookies(cookiesDownload);

            if (httprequest.getMethod().equals(HttpMethod.GET)) {
                writeResponse(ctx, msg);
                reset();
                return;
            }
            try {
                decoder = new HttpPostRequestDecoder(factory, httprequest);
            } catch (ErrorDataDecoderException ex) {
                log.log(Level.SEVERE, "HttpServerHandler exception", ex);
                writeResponse(ctx, msg);
                return;
            } catch (IncompatibleDataDecoderException ex) {
                writeResponse(ctx, msg);
                return;
            }
        }

        if (decoder != null) {
            if (msg instanceof HttpContent) {
                HttpContent chunk = (HttpContent) msg;
                try {
                    decoder.offer(chunk);
                } catch (ErrorDataDecoderException ex) {
                    log.log(Level.SEVERE, "HttpServerHandler exception", ex);
                    writeResponse(ctx, msg);
                    return;
                }
                readHttpDataChunkByChunk();
                if (chunk instanceof LastHttpContent) {
                    writeResponse(ctx, msg);
                    reset();
                }

            }
        }
    }

    private void readHttpDataChunkByChunk() {
        Map<String, List<String>> postmap = new TreeMap<>();
        try {
            while (decoder.hasNext()) {
                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    if (data.getHttpDataType() == HttpDataType.Attribute) {
                        try {
                            Attribute attribute = (Attribute) data;
                            List<String> list = new ArrayList<>();
                            list.add(attribute.getValue());
                            postmap.put(attribute.getName(), list);
                        } catch (IOException ex) {
                            log.log(Level.SEVERE, "HttpServerHandler exception", ex);
                        } finally {
                            data.release();
                        }
                    }
                }
            }

        } catch (EndOfDataDecoderException e) { // it's ok
        }
        request.updatePostParameters(postmap);
    }

    private void writeResponse(ChannelHandlerContext ctx, HttpObject msg) {

        Context sctx = new Context(request, app);
        resp = app.getRouter().forward(sctx);

        HttpResponse response = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1,
                true // ((LastHttpContent) msg).getDecoderResult().isSuccess()
                && resp != null
                        ? HttpResponseStatus.valueOf(resp.getStatus()) : HttpResponseStatus.BAD_REQUEST);

        response.headers().set(HttpHeaders.Names.TRANSFER_ENCODING, HttpHeaders.Values.CHUNKED);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, resp != null ? resp.getContentType() : "text/plain");
        
        resp.getHeaders().entrySet().stream().forEach((e) -> {
            response.headers().set(e.getKey(), e.getValue());
        });
        
        sctx.getRequest().getCookies().values().stream().forEach((c) -> {
            cookiesUpload.add(c);
        });
        
        Session session;
        if((session = sctx.getSession()) != null) {
            cookiesUpload.add(new DefaultCookie("SWEETIESESSIONID", session.getSessionId().toString()));
        }
        
        response.headers().set(HttpHeaders.Names.SET_COOKIE, ServerCookieEncoder.encode(cookiesUpload));
        
        if (resp.getContentLength() >= 0) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, resp.getContentLength());
        }

        if (HttpHeaders.isKeepAlive(httprequest)) {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        } else {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        }

        response.headers().set(HttpHeaders.Names.SERVER, "Sweetie/0.0.1.Alpha"); // how it's beautiful!

        ctx.write(response);

        if (resp.getStream() != null) {
            ctx.write(new HttpChunkedInput(new ChunkedStream(resp.getStream())));
        }
        LastHttpContent fs = new DefaultLastHttpContent();
        ChannelFuture sendContentFuture = ctx.writeAndFlush(fs);
        if (!HttpHeaders.isKeepAlive(httprequest)) {
            sendContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.log(Level.SEVERE, "HttpServerHandler exception", cause);
        ctx.close();
    }

}
