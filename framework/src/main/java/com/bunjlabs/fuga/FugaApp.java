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
package com.bunjlabs.fuga;

import com.bunjlabs.fuga.configuration.Configuration;
import com.bunjlabs.fuga.dependency.DependencyManager;
import com.bunjlabs.fuga.dependency.InjectException;
import com.bunjlabs.fuga.handlers.DefaultErrorHandler;
import com.bunjlabs.fuga.handlers.DefaultRequestHandler;
import com.bunjlabs.fuga.handlers.ErrorHandler;
import com.bunjlabs.fuga.handlers.RequestHandler;
import com.bunjlabs.fuga.network.HttpServer;
import com.bunjlabs.fuga.network.netty.NettyHttpServer;
import com.bunjlabs.fuga.resources.ResourceManager;
import com.bunjlabs.fuga.router.Router;
import com.bunjlabs.fuga.services.ServiceManager;
import com.bunjlabs.fuga.sessions.SessionManager;
import com.bunjlabs.fuga.sessions.SessionService;
import com.bunjlabs.fuga.views.DummyViewRenderer;
import com.bunjlabs.fuga.views.ViewRenderer;
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

    private final SessionManager sessionManager;
    private final ServiceManager serviceManager;

    private ViewRenderer viewRenderer;
    private RequestHandler requestHandler;
    private ErrorHandler errorHandler;

    private HttpServer httpserver;
    private SocketAddress addr;

    public FugaApp() {
        this.dependencyManager = new DependencyManager();

        this.resourceManager = new ResourceManager();
        this.configuration = new Configuration(this);
        this.router = new Router(this);
        this.sessionManager = new SessionManager(this);
        this.serviceManager = new ServiceManager(this);
    }

    private void start() throws Exception {
        log.info("Fuga Framework {}", configuration.get("fuga.version", "(version is unknown)"));

        dependencyManager.register(
                this,
                resourceManager,
                configuration,
                router,
                sessionManager,
                serviceManager
        );

        setViewRenderer(DummyViewRenderer.class);
        setRequestHandler(DefaultRequestHandler.class);
        setErrorHandler(DefaultErrorHandler.class);

        serviceManager.register(SessionService.class, configuration.getInt("fuga.sessions.refreshtime"), TimeUnit.SECONDS);

        prepare();

        addr = new InetSocketAddress(configuration.get("fuga.http.bindhost"), configuration.getInt("fuga.http.bindport"));
        httpserver = new NettyHttpServer(addr, this);
        httpserver.start();
    }

    public DependencyManager getDependencyManager() {
        return dependencyManager;
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

    public ViewRenderer getViewRenderer() {
        return viewRenderer;
    }

    public void setViewRenderer(ViewRenderer renderer) {
        this.viewRenderer = renderer;
        dependencyManager.register(ViewRenderer.class, renderer);
    }

    public void setViewRenderer(Class<? extends ViewRenderer> rendererClass) throws InjectException {
        this.viewRenderer = dependencyManager.registerAndInject(rendererClass);
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        dependencyManager.register(RequestHandler.class, requestHandler);
    }

    public void setRequestHandler(Class<? extends RequestHandler> requestHandlerClass) throws InjectException {
        this.requestHandler = dependencyManager.registerAndInject(requestHandlerClass);
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        dependencyManager.register(ErrorHandler.class, errorHandler);
    }

    public void setErrorHandler(Class<? extends ErrorHandler> errorHandlerClass) throws InjectException {
        this.errorHandler = dependencyManager.registerAndInject(errorHandlerClass);
    }

    public abstract void prepare() throws Exception;

    public static void launch(Class<? extends FugaApp> appClass) {
        try {
            FugaApp app = appClass.newInstance();
            app.start();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
