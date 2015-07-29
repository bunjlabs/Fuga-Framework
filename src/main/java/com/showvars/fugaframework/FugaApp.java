package com.showvars.fugaframework;

import com.showvars.fugaframework.configuration.Configuration;
import com.showvars.fugaframework.network.HttpServer;
import com.showvars.fugaframework.router.Router;
import com.showvars.fugaframework.services.ServiceManager;
import com.showvars.fugaframework.sessions.SessionManager;
import com.showvars.fugaframework.sessions.SessionService;
import com.showvars.fugaframework.templates.TemplateEngine;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class FugaApp {

    private final Configuration config;
    private final Router hmap = new Router();
    private final SocketAddress addr;
    private final SessionManager sessionManager;
    private final HttpServer httpserver;
    private final TemplateEngine templateEngine;
    private final ServiceManager serviceManager;
    private final Map<String, Object> objects;

    private FugaApp() {
        this.config = new Configuration();
        this.addr = new InetSocketAddress(config.get("fuga.http.bindhost", "localhost"), config.getInt("fuga.http.bindport", 8080));
        this.sessionManager = new SessionManager(this);
        this.httpserver = new HttpServer(addr, this);
        this.templateEngine = new TemplateEngine(this);
        this.serviceManager = new ServiceManager(this);
        this.objects = new HashMap<>();

    }

    public Router getRouter() {
        return hmap;
    }

    public void start() throws Exception {
        serviceManager.registerService(new SessionService(this), 15, TimeUnit.SECONDS);

        httpserver.start();
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
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

    public Configuration getConfiguration() {
        return config;
    }

    public abstract void prepare();
}
