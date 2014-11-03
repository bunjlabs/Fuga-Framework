package com.showvars.sweetie.foundation;

import com.showvars.sweetie.SweetieApp;

public class Context {

    private final Request request;
    private final SweetieApp app;

    public Context(Request request, SweetieApp app) {
        this.request = request;
        this.app = app;
    }

    public Request getRequest() {
        return request;
    }

    public SweetieApp getApp() {
        return app;
    }
}
