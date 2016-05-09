/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fugaframework.network;

import com.bunjlabs.fugaframework.FugaApp;
import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.Request;
import com.bunjlabs.fugaframework.foundation.RequestMethod;
import com.bunjlabs.fugaframework.foundation.Response;
import com.bunjlabs.fugaframework.foundation.Responses;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.stream.ChunkedStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final Logger log = LogManager.getLogger(HttpServerHandler.class);
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
    private final FugaApp app;
    private final String serverVersion;
    private ByteBuf content;
    private HttpRequest httprequest;
    private Request.Builder requestBuilder;
    private Response resp;
    private boolean decoder;
    private Collection<Cookie> cookiesUpload;

    private Map<String, List<String>> postmap;

    HttpServerHandler(FugaApp app) {
        this.app = app;
        content = Unpooled.buffer();

        serverVersion = app.getConfiguration().get("fuga.version", "(version is unknown)");
    }

    private void reset() {
        httprequest = null;
        requestBuilder = null;
        resp = null;
        decoder = false;
        cookiesUpload = null;
        content = Unpooled.buffer();

        postmap = null;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpRequest) {
            this.httprequest = (HttpRequest) msg;

            requestBuilder = new Request.Builder();

            // Decode URI GET query parameters
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httprequest.getUri());

            Map<String, List<Cookie>> cookiesDownload = new HashMap<>();
            cookiesUpload = new ArrayList<>();

            String cookieString = httprequest.headers().get(HttpHeaders.Names.COOKIE);
            if (cookieString != null) {
                ServerCookieDecoder.STRICT.decode(cookieString).stream().forEach((cookie) -> {
                    if (cookiesDownload.containsKey(cookie.name())) {
                        cookiesDownload.get(cookie.name()).add(cookie);
                    } else {
                        cookiesDownload.put(cookie.name(), new ArrayList<>(Arrays.asList(cookie)));
                    }
                });
            }

            postmap = new TreeMap<>();

            Map<String, String> headers = new HashMap<>();
            for (Map.Entry<String, String> e : httprequest.headers().entries()) {
                headers.put(e.getKey(), e.getValue());
            }

            requestBuilder.requestMethod(RequestMethod.valueOf(httprequest.getMethod().name()))
                    .headers(headers)
                    .uri(httprequest.getUri())
                    .path(queryStringDecoder.path())
                    .socketAddress(ctx.channel().remoteAddress())
                    .query(queryStringDecoder.parameters())
                    .parameters(postmap)
                    .cookiesDownload(cookiesDownload)
                    .cookiesUpload(new HashMap<>())
                    .content(content);

            if (httprequest.getMethod().equals(HttpMethod.GET)) {
                writeResponse(ctx, msg);
                reset();
                return;
            }
            decoder = true;
        }

        if (msg instanceof HttpContent && decoder) {
            HttpContent httpContent = (HttpContent) msg;

            if (httprequest != null && (httprequest.headers().contains("Content-Type", "application/form-data", true)
                    || httprequest.headers().contains("Content-Type", "application/x-www-form-urlencoded", true))) {
                HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(httprequest);

                try {
                    postDecoder.offer(httpContent);
                } catch (ErrorDataDecoderException ex) {
                    log.catching(ex);
                    writeResponse(ctx, msg);
                    reset();
                    return;
                }

                readHttpDataChunkByChunk(postDecoder);
            } else {
                content.writeBytes(httpContent.content());
            }

            if (httpContent instanceof LastHttpContent) {
                writeResponse(ctx, msg);
                reset();
                return;
            }
        }
    }

    private void readHttpDataChunkByChunk(HttpPostRequestDecoder decoder) {

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
                            log.catching(ex);
                        } finally {
                            data.release();
                        }
                    }
                }
            }

        } catch (EndOfDataDecoderException e) { // it's ok
        }

    }

    private void writeResponse(ChannelHandlerContext ctx, HttpObject msg) {
        Request request = requestBuilder.build();

        Context sctx = new Context(request, app);

        log.debug("Access from {}: {}{}", request.getSocketAddress(), request.getHost(), request.getUri());

        resp = app.getRequestHandler().onRequest(sctx);

        HttpResponse response;

        if (resp == null) {
            log.error("Null response is received");

            resp = Responses.internalServerError();
        }

        response = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.valueOf(resp.getStatus()));

        response.headers().set(HttpHeaders.Names.TRANSFER_ENCODING, HttpHeaders.Values.CHUNKED);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, resp.getContentType());

        // Disable cache by default
        response.headers().set(HttpHeaders.Names.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        response.headers().set(HttpHeaders.Names.PRAGMA, "no-cache");
        response.headers().set(HttpHeaders.Names.EXPIRES, "0");

        resp.getHeaders().entrySet().stream().forEach((e) -> {
            response.headers().set(e.getKey(), e.getValue());
        });

        response.headers().set(HttpHeaders.Names.SERVER, "Fuga Web Server/" + serverVersion); // how it's beautiful!

        // Set cookies
        cookiesUpload.addAll(sctx.getRequest().getCookiesUpload().values());
        response.headers().set(HttpHeaders.Names.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookiesUpload));

        if (resp.getContentLength() >= 0) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, resp.getContentLength());
        }

        if (HttpHeaders.isKeepAlive(httprequest)) {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        } else {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        }

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
        log.catching(cause);
        ctx.close();
        reset();
    }

}
