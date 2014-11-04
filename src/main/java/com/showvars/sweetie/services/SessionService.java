package com.showvars.sweetie.services;

import com.showvars.sweetie.SweetieApp;
import com.showvars.sweetie.foundation.Session;
import com.showvars.sweetie.network.HttpServer;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class SessionService extends Service {

    private static final Logger log = Logger.getLogger(HttpServer.class.getName());
    private final Map<UUID, Session> sessions;

    public SessionService(SweetieApp app) {
        this.sessions = app.getSessions();
    }

    @Override
    public void run() {
        sessions.entrySet().stream().forEach((e) -> {
            Session session = e.getValue();
            long currentTime = new Date().getTime();
            if (currentTime - session.getCreationTime() >= session.getTimeout() * 60000) {
                sessions.remove(e.getKey());
            }
        });
    }

}
