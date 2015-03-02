package com.showvars.sweetie.sessions;

import com.showvars.sweetie.SweetieApp;
import com.showvars.sweetie.foundation.Context;
import io.netty.handler.codec.http.Cookie;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private final Map<UUID, Session> sessions = new HashMap<>();
    private final SweetieApp app;

    public SessionManager(SweetieApp app) {
        this.app = app;
    }

    public Session getSession(UUID sessionId) {
        return sessions.get(sessionId);
    }

    public Session getSession(String sessionId) {
        return sessions.get(UUID.fromString(sessionId));
    }

    public void process(Context ctx) {
        Cookie sessionCookie = ctx.getRequest().getCookie("SWEETIESESSIONID");
        Session session;
        if (sessionCookie == null || (session = sessions.get(UUID.fromString(sessionCookie.getValue()))) == null) {
            UUID sessionId = UUID.randomUUID();
            session = new Session(sessionId);

            sessions.put(sessionId, session);
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
