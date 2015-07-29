package com.showvars.fugaframework.foundation;

import com.showvars.fugaframework.FugaApp;
import com.showvars.fugaframework.sessions.Session;

public class Context {

    private final Request request;
    private final FugaApp app;
    private Session session;

    public Context(Request request, FugaApp app) {
        this.request = request;
        this.app = app;
    }

    public Request getRequest() {
        return request;
    }

    public FugaApp getApp() {
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
