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
package com.bunjlabs.fuga.network.netty;

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.foundation.content.BufferedContent;
import com.bunjlabs.fuga.foundation.Cookie;
import com.bunjlabs.fuga.foundation.Request;
import com.bunjlabs.fuga.foundation.RequestMethod;
import com.bunjlabs.fuga.foundation.Response;
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
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.stream.ChunkedStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final Logger log = LogManager.getLogger(NettyHttpServerHandler.class);
    private final FugaApp app;
    private final String serverVersion;
    private final int forwarded;
    private ByteBuf contentBuffer;
    private HttpRequest httprequest;
    private Request.Builder requestBuilder;
    private boolean decoder;

    NettyHttpServerHandler(FugaApp app) {
        this.app = app;
        this.contentBuffer = Unpooled.buffer();

        this.serverVersion = app.getConfiguration().get("fuga.version");
        this.forwarded = app.getConfiguration().get("fuga.http.forwarded").equals("rfc7239") ? 1 : 0;
    }

    private void reset() {
        this.httprequest = null;
        this.requestBuilder = null;
        this.decoder = false;
        this.contentBuffer = Unpooled.buffer();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpRequest) {
            this.httprequest = (HttpRequest) msg;

            requestBuilder = new Request.Builder();

            requestBuilder.requestMethod(RequestMethod.valueOf(httprequest.method().name()))
                    .uri(httprequest.uri())
                    .cookiesUpload(new HashMap<>());

            try {

                // Decode URI GET query parameters
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httprequest.uri());
                requestBuilder.path(queryStringDecoder.path()).query(queryStringDecoder.parameters());

                // Process cookies
                Map<String, List<Cookie>> cookiesDownload = new HashMap<>();

                String cookieString = httprequest.headers().get(HttpHeaderNames.COOKIE);
                if (cookieString != null) {
                    ServerCookieDecoder.STRICT.decode(cookieString).stream().forEach((cookie) -> {
                        if (cookiesDownload.containsKey(cookie.name())) {
                            cookiesDownload.get(cookie.name()).add(NettyCookieConverter.convertToFuga(cookie));
                        } else {
                            cookiesDownload.put(cookie.name(), new ArrayList<>(NettyCookieConverter.convertListToFuga(cookie)));
                        }
                    });
                }

                requestBuilder.cookiesDownload(cookiesDownload);

                // Process headers
                Map<String, String> headers = new HashMap<>();
                httprequest.headers().entries().stream().forEach((e) -> {
                    headers.put(e.getKey(), e.getValue());
                });

                requestBuilder.headers(headers);

                // Get real parameters from frontend HTTP server
                boolean isSecure = false;
                SocketAddress remoteAddress = ctx.channel().remoteAddress();

                if (forwarded == 1) { // RFC7239
                    if (headers.containsKey("Forwarded")) {
                        String fwdStr = headers.get("Forwarded");

                        List<String> fwdparams = Stream.of(fwdStr.split("; ")).map((s) -> s.trim()).collect(Collectors.toList());

                        for (String f : fwdparams) {
                            String p[] = f.split("=");

                            switch (p[0]) {
                                case "for":
                                    remoteAddress = parseAddress(p[1]);
                                    break;
                                case "proto":
                                    isSecure = p[1].equals("https");
                                    break;
                            }
                        }
                    }
                } else if (forwarded == 0) { // X-Forwarded
                    if (headers.containsKey("X-Forwarded-Proto")) {
                        if (headers.get("X-Forwarded-Proto").equalsIgnoreCase("https")) {
                            isSecure = true;
                        }
                    }

                    if (headers.containsKey("X-Forwarded-For")) {
                        String fwdfor = headers.get("X-Forwarded-For");
                        remoteAddress = parseAddress(fwdfor.contains(",") ? fwdfor.substring(0, fwdfor.indexOf(',')) : fwdfor);
                    } else if (headers.containsKey("X-Real-IP")) {
                        remoteAddress = parseAddress(headers.get("X-Real-IP"));
                    }
                }

                requestBuilder.remoteAddress(remoteAddress).isSecure(isSecure);

                if (httprequest.method().equals(HttpMethod.GET)) {
                    processResponse(ctx);
                    return;
                }
                decoder = true;
            } catch (Exception e) {
                processClientError(ctx, requestBuilder.build(), 400);
                return;
            }
        }

        if (msg instanceof HttpContent && decoder) {
            HttpContent httpContent = (HttpContent) msg;

            contentBuffer.writeBytes(httpContent.content());

            if (httpContent instanceof LastHttpContent) {
                requestBuilder.content(new BufferedContent(contentBuffer.nioBuffer()));
                processResponse(ctx);
            }
        }
    }

    private void processClientError(ChannelHandlerContext ctx, Request request, int code) {
        Response response = app.getErrorHandler().onClientError(request, code);

        writeResponse(ctx, request, response);
        reset();
    }

    private void processServerError(ChannelHandlerContext ctx, Request request, Throwable cause) {
        Response response = app.getErrorHandler().onServerError(request, cause);

        writeResponse(ctx, request, response);
        reset();
    }

    private void processResponse(ChannelHandlerContext ctx) {
        Request request = requestBuilder.build();
        Response response = app.getRequestHandler().onRequest(request);

        if (response == null) {
            log.error("Null response is received");

            processServerError(ctx, request, new NullPointerException("Null response is received"));
            return;
        }

        writeResponse(ctx, request, response);
        reset();
    }

    private void writeResponse(ChannelHandlerContext ctx, Request request, Response response) {
        HttpResponse httpresponse = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.valueOf(response.getStatus()));

        httpresponse.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
        httpresponse.headers().set(HttpHeaderNames.CONTENT_TYPE, response.getContentType());

        // Disable cache by default
        httpresponse.headers().set(HttpHeaderNames.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        httpresponse.headers().set(HttpHeaderNames.PRAGMA, "no-cache");
        httpresponse.headers().set(HttpHeaderNames.EXPIRES, "0");

        response.getHeaders().entrySet().stream().forEach((e)
                -> httpresponse.headers().set(e.getKey(), e.getValue())
        );

        httpresponse.headers().set(HttpHeaderNames.SERVER, "Fuga Netty Web Server/" + serverVersion);

        // Set cookies
        List<Cookie> cookiesUpload = new ArrayList<>();
        cookiesUpload.addAll(request.getCookiesUpload().values());
        httpresponse.headers().set(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(NettyCookieConverter.convertListToNetty(cookiesUpload)));

        if (response.getContentLength() >= 0) {
            httpresponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.getContentLength());
        }

        if (HttpUtil.isKeepAlive(httprequest)) {
            httpresponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        } else {
            httpresponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        }

        ctx.write(httpresponse);

        if (response.getStream() != null) {
            ctx.write(new HttpChunkedInput(new ChunkedStream(response.getStream())));
        }

        LastHttpContent fs = new DefaultLastHttpContent();
        ChannelFuture sendContentFuture = ctx.writeAndFlush(fs);
        if (!HttpUtil.isKeepAlive(httprequest)) {
            sendContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.catching(cause);
        ctx.close();
        reset();
    }

    private static InetSocketAddress parseAddress(String addr) {
        String ip = addr.contains(":") ? addr.substring(0, addr.lastIndexOf(':')) : addr;
        String port = addr.contains(":") ? addr.substring(addr.lastIndexOf(':') + 1) : "0";
        return new InetSocketAddress(ip, Integer.parseInt(port));
    }

}
