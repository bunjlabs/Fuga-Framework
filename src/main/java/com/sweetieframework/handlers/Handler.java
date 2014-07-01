package com.sweetieframework.handlers;

import com.sweetieframework.foundation.Response;
import io.netty.handler.codec.http.HttpRequest;
 
public abstract class Handler {
 
    public abstract Response process(HttpRequest request);
 
    //public String getContentType() {
    //    return "text/plain; charset=UTF-8";
    //}
}
