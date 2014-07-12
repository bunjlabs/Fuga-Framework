package com.sweetieframework.handlers;

import com.sweetieframework.foundation.Request;
import com.sweetieframework.foundation.Response;
import java.util.List;
 
public abstract class Handler {
 
    public abstract Response process(Request request, List<String> matches);
    
}
