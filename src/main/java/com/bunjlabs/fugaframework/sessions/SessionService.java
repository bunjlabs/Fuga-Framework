package com.bunjlabs.fugaframework.sessions;

import com.bunjlabs.fugaframework.FugaApp;
import com.bunjlabs.fugaframework.network.HttpServer;
import com.bunjlabs.fugaframework.services.Service;
import java.util.logging.Logger;

public class SessionService extends Service {

    private static final Logger log = Logger.getLogger(HttpServer.class.getName());
    private final SessionManager sessionManager;

    public SessionService(FugaApp app) {
        this.sessionManager = app.getSessionManager();
    }

    @Override
    public void run() {
        sessionManager.update();
    }

}
