package com.sweetieframework;

import com.sweetieframework.foundation.network.HttpServer;
import com.sweetieframework.handlers.HandlersMap;

public class SweetieServer {
    private final HandlersMap hmap = new HandlersMap();
    private HttpServer httpserver;
    private SweetieConfiguration conf;

    SweetieServer() {

    }
    
    public HandlersMap getHandlersMap() {
        return hmap;
    }
    
    public void bind() {
        httpserver = new HttpServer(null, this);
    }
}
