/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fugaframework;

import com.bunjlabs.fugaframework.configuration.Configuration;
import com.bunjlabs.fugaframework.dependency.DependencyManager;
import com.bunjlabs.fugaframework.network.HttpServer;
import com.bunjlabs.fugaframework.resources.ResourceManager;
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

    private final DependencyManager dependencyManager;
    private final ResourceManager resourceManager;
    private final Configuration configuration;
    private final Router router;
    private final TemplateEngine templateEngine;

    private final SessionManager sessionManager;
    private final ServiceManager serviceManager;

    private HttpServer httpserver;
    private SocketAddress addr;

    public FugaApp() {
        this.dependencyManager = new DependencyManager(this);

        this.resourceManager = new ResourceManager();
        this.configuration = new Configuration(this);
        this.router = new Router(this);
        this.templateEngine = new TemplateEngine(this);
        this.sessionManager = new SessionManager(this);
        this.serviceManager = new ServiceManager(this);

    }

    public void start() throws Exception {
        log.info("Fuga Framework {}", configuration.get("fuga.version", "(version is unknown)"));

        dependencyManager.registerDependency(this, resourceManager, configuration, router, templateEngine, sessionManager, serviceManager);

        serviceManager.registerService(SessionService.class, configuration.getInt("fuga.sessions.refreshtime"), TimeUnit.SECONDS);

        prepare();

        addr = new InetSocketAddress(configuration.get("fuga.http.bindhost"), configuration.getInt("fuga.http.bindport"));
        httpserver = new HttpServer(addr, this);
        httpserver.start();
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Router getRouter() {
        return router;
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

    public DependencyManager getDependencyManager() {
        return dependencyManager;
    }

    public abstract void prepare();
}
