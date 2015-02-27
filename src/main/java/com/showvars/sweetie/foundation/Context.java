package com.showvars.sweetie.foundation;

import com.showvars.sweetie.SweetieApp;
import com.showvars.sweetie.sessions.Session;

public class Context {

    private final Request request;
    private final SweetieApp app;
    private Session session;

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

    public Session getSession() {
        if (session == null) {
            app.getSessionManager().process(this);
        }
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
