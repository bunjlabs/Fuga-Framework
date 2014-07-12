package com.sweetieframework.foundation;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

public class Request {

    private final RequestMethod method;
    private final String uri;
    private final SocketAddress socketAddress;
    private final Map<String, List<String>> params;
    
    public Request(RequestMethod method, String uri, SocketAddress socketAddress, Map<String, List<String>> params){
        this.method = method;
        this.uri = uri;
        this.socketAddress = socketAddress;
        this.params = params;
        
    }
    
    public RequestMethod getRequestMethod() {
        return method;
    }
    
    public String getUri() {
        return uri;
    }
    
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }
    
    public Map<String, List<String>> getParams() {
        return params;
    }
}
