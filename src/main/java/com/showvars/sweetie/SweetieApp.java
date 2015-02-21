package com.showvars.sweetie;

import com.showvars.sweetie.configuration.SweetieConfiguration;
import com.showvars.sweetie.foundation.Session;
import com.showvars.sweetie.network.HttpServer;
import com.showvars.sweetie.router.Router;
import com.showvars.sweetie.services.ServiceManager;
import com.showvars.sweetie.services.SessionService;
import com.showvars.sweetie.templates.TemplateEngine;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SweetieApp {

    private final SweetieConfiguration config;
    private final Router hmap = new Router();
    private final SocketAddress addr;
    private final Map<UUID, Session> sessions;
    private final HttpServer httpserver;
    private final TemplateEngine templateEngine;
    private final ServiceManager serviceManager;
    private final Map<String, Object> objects;

    private SweetieApp() {
        this.config = new SweetieConfiguration();
        this.addr = new InetSocketAddress(config.get("sweetie.http.bindhost", "localhost"), config.getInt("sweetie.http.bindport", 8080));
        this.sessions = new HashMap<>();
        this.httpserver = new HttpServer(addr, this);
        this.templateEngine = new TemplateEngine(this);
        this.serviceManager = new ServiceManager(this);
        this.objects = new HashMap<>();

    }

    public Router getRouter() {
        return hmap;
    }

    public void getStarted() throws Exception {
        serviceManager.registerService(new SessionService(this), 15, TimeUnit.SECONDS);

        httpserver.start();
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public Session getSession(UUID uuid) {
        return sessions.get(uuid);
    }

    public Map<UUID, Session> getSessions() {
        return sessions;
    }

    public void putSession(UUID uuid, Session session) {
        sessions.put(uuid, session);
    }

    public Object getObject(String name) {
        return objects.get(name);
    }

    public void putObject(String name, Object obj) {
        objects.put(name, obj);
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public SweetieConfiguration getConfiguration() {
        return config;
    }

    public static SweetieApp prepare() {
        SweetieApp ss = new SweetieApp();
        return ss;
    }
}
