package com.showvars.sweetie.foundation;

import io.netty.handler.codec.http.Cookie;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private RequestMethod requestMethod;
    private String host;
    private String uri;
    private String path;
    private SocketAddress socketAddress;
    private final Map<String, List<String>> getParameters;
    private final Map<String, List<String>> postParameters;
    //private Cookies cookies;
    private Map<String, Cookie> cookiesDownload;
    private Map<String, Cookie> cookiesUpload;

    public Request(RequestMethod method,
            String host,
            String uri,
            String path,
            SocketAddress socketAddress) {
        this.requestMethod = method;
        this.host = host;
        this.uri = uri;
        this.path = path;
        this.socketAddress = socketAddress;

        this.getParameters = new HashMap<>();
        this.postParameters = new HashMap<>();

        this.cookiesDownload = new HashMap<>();
        this.cookiesUpload = new HashMap<>();

    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getHost() {
        return host;
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

    public Map<String, List<String>> getParameters() {
        return getParameters;
    }

    public Map<String, List<String>> getPostParameters() {
        return postParameters;
    }

    public Cookie getCookie(String name) {
        return cookiesDownload.get(name);
    }

    public Map<String, Cookie> getCookiesDownload() {
        return cookiesDownload;
    }

    public Map<String, Cookie> getCookiesUpload() {
        return cookiesUpload;
    }

    public void setMethod(RequestMethod method) {
        this.requestMethod = method;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public void setParameters(Map<String, List<String>> getParameters) {
        if (getParameters != null) {
            this.getParameters.clear();
            this.getParameters.putAll(getParameters);
        }
    }

    public void updateParameters(Map<String, List<String>> postparams) {
        this.getParameters.putAll(postparams);
    }

    public void setPostParameters(Map<String, List<String>> postparams) {
        if (postparams != null) {
            this.postParameters.clear();
            this.postParameters.putAll(postparams);
        }
    }

    public void updatePostParameters(Map<String, List<String>> postparams) {
        this.postParameters.putAll(postparams);
    }

    public void setCookie(Cookie cookie) {
        this.cookiesUpload.put(cookie.getName(), cookie);
    }

    public void setCookiesDownload(Map<String, Cookie> cookies) {
        cookiesDownload = cookies;
    }
}
