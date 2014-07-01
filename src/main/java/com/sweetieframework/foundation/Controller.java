package com.sweetieframework.foundation;

import com.sweetieframework.handlers.Handler;
import java.io.File;
import java.io.InputStream;

public abstract class Controller extends Handler {

    protected static Response ans() {
        return new Response();
    }
    
    protected static Response ans(String s) {
        return new Response(s);
    }
    
    protected static Response ans(File f) {
        return new Response(f);
    }
    
    protected static Response ans(InputStream is) {
        return new Response(is);
    }
}
