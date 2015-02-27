package com.showvars.sweetie.sessions;

import com.showvars.sweetie.SweetieApp;
import com.showvars.sweetie.network.HttpServer;
import com.showvars.sweetie.services.Service;
import java.util.logging.Logger;

public class SessionService extends Service {

    private static final Logger log = Logger.getLogger(HttpServer.class.getName());
    private final SessionManager sessionManager;

    public SessionService(SweetieApp app) {
        this.sessionManager = app.getSessionManager();
    }

    @Override
    public void run() {
        sessionManager.update();
    }

}
