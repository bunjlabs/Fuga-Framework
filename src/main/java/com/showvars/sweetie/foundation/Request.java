package com.showvars.sweetie.foundation;

import io.netty.handler.codec.http.Cookie;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private RequestMethod requestMethod;
    private String uri;
    private String path;
    private SocketAddress socketAddress;
    private final Map<String, List<String>> getParameters;
    private final Map<String, List<String>> postParameters;
    private final Map<String, String> session;
    private final Map<String, Cookie> cookies;

    public Request(RequestMethod method,
            String uri,
            String path,
            SocketAddress socketAddress) {
        this.requestMethod = method;
        this.uri = uri;
        this.path = path;
        this.socketAddress = socketAddress;

        this.getParameters = new HashMap<>();
        this.postParameters = new HashMap<>();
        this.session = new HashMap<>();
        this.cookies = new HashMap<>();

    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
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

    public Map<String, String> getSession() {
        return session;
    }
    
    public Map<String, Cookie> getCookies() {
        return cookies;
    }

    public void setMethod(RequestMethod method) {
        this.requestMethod = method;
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

    public void setPostParameters(Map<String, List<String>> postparams) {
        if (postparams != null) {
            this.postParameters.clear();
            this.postParameters.putAll(postparams);
        }
    }
    
    public void setSession(Map<String, String> session) {
        if (session != null) {
            this.session.clear();
            this.session.putAll(session);
        }
    }

    public void setCookies(Map<String, Cookie> cookies) {
        if (cookies != null) {
            this.cookies.clear();
            this.cookies.putAll(cookies);
        }
    }
}
