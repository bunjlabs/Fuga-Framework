package com.bunjlabs.fugaframework.sessions;

import com.bunjlabs.fugaframework.FugaApp;
import com.bunjlabs.fugaframework.foundation.Context;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.DefaultCookie;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private final Map<UUID, Session> sessions = new HashMap<>();
    private final FugaApp app;

    public SessionManager(FugaApp app) {
        this.app = app;
    }

    public Session getSession(UUID sessionId) {
        return sessions.get(sessionId);
    }

    public Session getSession(String sessionId) {
        return sessions.get(UUID.fromString(sessionId));
    }

    public void process(Context ctx) {
        List<Cookie> sessionCookieList = ctx.getRequest().getCookiesDownload().get("FUGASESSIONID");
        Session session = null;
        
        boolean flag = false;
        if (sessionCookieList != null) {
            for (Cookie sessionCookie : sessionCookieList) {
                if (sessionCookie != null && (session = sessions.get(UUID.fromString(sessionCookie.getValue()))) != null) {
                    flag = true;
                    break;
                }
            }
        }

        if (!flag) {
            UUID sessionId = UUID.randomUUID();
            session = new Session(sessionId);

            sessions.put(sessionId, session);

            DefaultCookie sessionCookie = new DefaultCookie("FUGASESSIONID", session.getSessionId().toString());
            sessionCookie.setPath("/");
            ctx.getRequest().setCookie(sessionCookie);
        }
        ctx.setSession(session);
    }

    public void update() {
        sessions.entrySet().stream().forEach((e) -> {
            Session session = e.getValue();
            long currentTime = new Date().getTime();
            if (currentTime - session.getUpdateTime() >= session.getTimeout() * 60000) {
                sessions.remove(e.getKey());
            }
        });
    }

}
