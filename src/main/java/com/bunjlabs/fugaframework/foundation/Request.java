package com.bunjlabs.fugaframework.foundation;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.cookie.Cookie;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

public class Request {

    private final RequestMethod requestMethod;
    private Map<String, String> headers;
    private final String uri;
    private final String path;
    private final SocketAddress socketAddress;
    private final Map<String, List<String>> query;
    private final Map<String, List<String>> parameters;
    private final Map<String, List<Cookie>> cookiesDownload;
    private final Map<String, Cookie> cookiesUpload;
    private final ByteBuf content;
    private String s;

    public static class Builder {

        private RequestMethod requestMethod;
        private Map<String, String> headers;
        private String uri;
        private String path;
        private SocketAddress socketAddress;
        private Map<String, List<String>> query;
        private Map<String, List<String>> parameters;
        private Map<String, List<Cookie>> cookiesDownload;
        private Map<String, Cookie> cookiesUpload;
        private ByteBuf content;

        public Builder requestMethod(RequestMethod requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder socketAddress(SocketAddress socketAddress) {
            this.socketAddress = socketAddress;
            return this;
        }

        public Builder query(Map<String, List<String>> query) {
            this.query = query;
            return this;
        }

        public Builder parameters(Map<String, List<String>> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder cookiesDownload(Map<String, List<Cookie>> cookiesDownload) {
            this.cookiesDownload = cookiesDownload;
            return this;
        }

        public Builder cookiesUpload(Map<String, Cookie> cookiesUpload) {
            this.cookiesUpload = cookiesUpload;
            return this;
        }

        public Builder content(ByteBuf content) {
            this.content = content;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

    private Request(Builder builder) {
        this.requestMethod = builder.requestMethod;
        this.headers = builder.headers;
        this.uri = builder.uri;
        this.path = builder.path;
        this.socketAddress = builder.socketAddress;
        this.query = builder.query;
        this.parameters = builder.parameters;
        this.cookiesDownload = builder.cookiesDownload;
        this.cookiesUpload = builder.cookiesUpload;
        this.content = builder.content;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getHost() {
        return headers.get("Host");
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getUri() {
        return uri;
    }

    public String getPath() {
        return path;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public Map<String, List<String>> getQuery() {
        return query;
    }

    public Map<String, List<String>> getParameters() {
        return parameters;
    }

    public Cookie getCookie(String name) {
        List<Cookie> cookies = cookiesDownload.get(name);

        if (cookies == null) {
            return null;
        }

        return cookies.get(0);
    }

    public Map<String, List<Cookie>> getCookiesDownload() {
        return cookiesDownload;
    }

    public Map<String, Cookie> getCookiesUpload() {
        return cookiesUpload;
    }

    public void setCookie(Cookie cookie) {
        this.cookiesUpload.put(cookie.name(), cookie);
    }

    public ByteBuf getContent() {
        return content;
    }
}
