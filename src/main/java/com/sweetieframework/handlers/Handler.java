package com.sweetieframework.handlers;

import com.sweetieframework.foundation.Response;
import io.netty.handler.codec.http.HttpRequest;
import java.util.List;
 
public abstract class Handler {
 
    public abstract Response process(HttpRequest request, List<String> matches);
    
}
