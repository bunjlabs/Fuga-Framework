package com.sweetieframework.foundation;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private RequestMethod requestMethod;
    private String uri;
    private String path;
    private SocketAddress socketAddress;
    private Map<String, List<String>> getParameters;
    private Map<String, List<String>> postParameters;
    private List<String> matches;

    public Request(RequestMethod method,
            String uri,
            String path,
            SocketAddress socketAddress,
            List<String> matches,
            Map<String, List<String>> getparams,
            Map<String, List<String>> postparams) {
        this.requestMethod = method;
        this.uri = uri;
        this.path = path;
        this.socketAddress = socketAddress;

        this.getParameters = new HashMap<>();
        if (getparams != null) {
            this.getParameters.putAll(getparams);
        }

        this.postParameters = new HashMap<>();
        if (postparams != null) {
            this.postParameters.putAll(postparams);
        }

        this.matches = new ArrayList<>();
        if (matches != null) {
            this.matches.addAll(matches);
        }
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

    public List<String> getMatches() {
        return matches;
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

    public void setParameters(Map<String, List<String>> getparams) {
        this.getParameters = getparams;
    }

    public void setPostParameters(Map<String, List<String>> postparams) {
        this.postParameters = postparams;
    }

    public void setMatches(List<String> matches) {
        this.matches = matches;
    }
}
