package com.bunjlabs.fugaframework.router;

public class RoutesMapSyntaxException extends Exception {
    public RoutesMapSyntaxException(Tokenizer t, String msg) {
        super("(" + t.line + ":" + t.column + ") " + msg);
    }
}
