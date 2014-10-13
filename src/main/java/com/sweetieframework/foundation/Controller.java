package com.sweetieframework.foundation;

import java.io.File;
import java.io.InputStream;

public abstract class Controller {

    protected static Response ok() {
        return new Response();
    }
    
    protected static Response ok(String s) {
        return new Response(s);
    }
    
    protected static Response ok(File f) {
        return new Response(f);
    }
    
    protected static Response ok(InputStream is) {
        return new Response(is);
    }
    
    protected static Response redirect(String url) {
        return new Response().setStatus(302);
    }
    
    protected static Response temporaryRedirect(String uri) {
        return new Response();
    }
}
