package com.sweetieframework.handlers;

import java.util.List;

public class HandlerMatched {

    private final Handler handler;
    private final List<String> matches;

    HandlerMatched(Handler handler, List<String> matches) {
        this.handler = handler;
        this.matches = matches;
    }
    
    public Handler getHandler() {
        return handler;
    }
    public List<String> getMatches() {
        return matches;
    }
}
