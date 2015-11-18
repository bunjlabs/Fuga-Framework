package com.showvars.fugaframework.foundation;

import com.showvars.fugaframework.FugaApp;
import com.showvars.fugaframework.sessions.Session;
import java.util.HashMap;
import java.util.Map;

public class Context {

    private final Request request;
    private final FugaApp app;
    private final Map<String, Object> data;
    private Session session;

    public Context(Request request, FugaApp app) {
        this.request = request;
        this.app = app;
        data = new HashMap<>();
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

    public <T> T get(String name, Class<T> type) {
        return (T) data.get(name);
    }

    public Object get(String name) {
        return data.get(name);
    }

    public void put(String name, Object obj) {
        data.put(name, obj);
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
