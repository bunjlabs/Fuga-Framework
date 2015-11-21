package com.bunjlabs.fugaframework;

import com.bunjlabs.fugaframework.configuration.Configuration;
import com.bunjlabs.fugaframework.network.HttpServer;
import com.bunjlabs.fugaframework.router.Router;
import com.bunjlabs.fugaframework.services.ServiceManager;
import com.bunjlabs.fugaframework.sessions.SessionManager;
import com.bunjlabs.fugaframework.sessions.SessionService;
import com.bunjlabs.fugaframework.templates.TemplateEngine;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FugaApp {

    private static final Logger log = LogManager.getLogger(FugaApp.class);
    private final Configuration config;
    private final Router router;
    private final SessionManager sessionManager;
    private final ServiceManager serviceManager;
    private final TemplateEngine templateEngine;
    private HttpServer httpserver;
    private SocketAddress addr;

    public FugaApp() {
        this.config = new Configuration();

        this.router = new Router();
        this.sessionManager = new SessionManager();
        this.serviceManager = new ServiceManager();
        
        this.templateEngine = new TemplateEngine(this);

    }

    public Router getRouter() {
        return router;
    }

    public void start() throws Exception {
        log.info("Fuga Framework 0.3.0-SNAPSHOT");

        prepare();

        serviceManager.registerService(new SessionService(this), config.getInt("fuga.sessions.refreshtime", 15), TimeUnit.SECONDS);
        addr = new InetSocketAddress(config.get("fuga.http.bindhost", "localhost"), config.getInt("fuga.http.bindport", 8080));
        httpserver = new HttpServer(addr, this);
        httpserver.start();
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public Configuration getConfiguration() {
        return config;
    }

    public abstract void prepare();
}
