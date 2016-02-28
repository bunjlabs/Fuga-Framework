package com.bunjlabs.fugaframework.sessions;

import com.bunjlabs.fugaframework.dependency.Inject;
import com.bunjlabs.fugaframework.services.Service;

public class SessionService extends Service {

    @Inject
    public SessionManager sessionManager;

    @Override
    public void onUpdate() {
        sessionManager.update();
    }

}
