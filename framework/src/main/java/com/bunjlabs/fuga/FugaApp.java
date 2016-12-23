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
import com.bunjlabs.fuga.i18n.MessagesManager;
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

/**
 * A Fuga Application.
 */
public abstract class FugaApp {

    private final Logger log = LogManager.getLogger(this);

    private final DependencyManager dependencyManager;
    private final ResourceManager resourceManager;
    private final Configuration configuration;
    private final Router router;

    private final SessionManager sessionManager;
    private final ServiceManager serviceManager;
    private final MessagesManager messagesManager;

    private ViewRenderer viewRenderer;
    private RequestHandler requestHandler;
    private ErrorHandler errorHandler;

    private HttpServer httpserver;
    private SocketAddress addr;

    /**
     * Creates a new FugaApp.
     */
    public FugaApp() {
        this.dependencyManager = new DependencyManager();

        this.resourceManager = new ResourceManager();
        this.configuration = new Configuration(this);
        this.router = new Router(this);
        this.sessionManager = new SessionManager(this);
        this.serviceManager = new ServiceManager(this);
        this.messagesManager = new MessagesManager(this);
    }

    /**
     * Starts application.
     */
    private void start() throws Exception {
        log.info("Fuga Framework {}", configuration.get("fuga.version", "(version is unknown)"));

        dependencyManager.register(
                this,
                resourceManager,
                configuration,
                router,
                sessionManager,
                serviceManager,
                messagesManager,
                dependencyManager
        );

        setViewRenderer(DummyViewRenderer.class);
        setRequestHandler(DefaultRequestHandler.class);
        setErrorHandler(DefaultErrorHandler.class);

        prepare();

        messagesManager.load();
        serviceManager.register(SessionService.class, configuration.getInt("fuga.sessions.refreshtime"), TimeUnit.SECONDS);

        addr = new InetSocketAddress(configuration.get("fuga.http.bindhost"), configuration.getInt("fuga.http.bindport"));
        httpserver = new NettyHttpServer(addr, this);
        httpserver.start();
    }

    /**
     * Returns the current dependency manager.
     *
     * @return the dependency manager.
     */
    public DependencyManager getDependencyManager() {
        return dependencyManager;
    }

    /**
     * Returns the current resource manager.
     *
     * @return the current resource manager.
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    /**
     * Returns the current configuration.
     *
     * @return the current configuration.
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Returns the current router.
     *
     * @return the current router.
     */
    public Router getRouter() {
        return router;
    }

    /**
     * Returns the current service manager.
     *
     * @return the current service manager.
     */
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    /**
     * Returns the current session manager.
     *
     * @return the current session manager.
     */
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    /**
     * Returns the current messages manager.
     *
     * @return the current messages manager.
     */
    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    /**
     * Returns the current view renderer.
     *
     * @return the current view renderer.
     */
    public ViewRenderer getViewRenderer() {
        return viewRenderer;
    }

    /**
     * Set current view renderer object.
     *
     * @param renderer View renderer object.
     */
    public void setViewRenderer(ViewRenderer renderer) {
        this.viewRenderer = renderer;
        dependencyManager.register(ViewRenderer.class, renderer);
    }

    /**
     * Set current view renderer class.
     *
     * @param rendererClass View renderer class.
     * @throws InjectException if unable to inject dependencies
     */
    public void setViewRenderer(Class<? extends ViewRenderer> rendererClass) throws InjectException {
        this.viewRenderer = dependencyManager.registerAndInject(rendererClass);
    }

    /**
     * Returns the current request handler.
     *
     * @return the current request handler.
     */
    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    /**
     * Set current request handler object.
     *
     * @param requestHandler Request handler object.
     */
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        dependencyManager.register(RequestHandler.class, requestHandler);
    }

    /**
     *
     * Set current request handler class.
     *
     * @param requestHandlerClass Request handler class.
     * @throws InjectException if unable to inject dependencies
     */
    public void setRequestHandler(Class<? extends RequestHandler> requestHandlerClass) throws InjectException {
        this.requestHandler = dependencyManager.registerAndInject(requestHandlerClass);
    }

    /**
     * Returns the current error handler.
     *
     * @return the current error handler.
     */
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Set current error handler object.
     *
     * @param errorHandler Error handler object.
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        dependencyManager.register(ErrorHandler.class, errorHandler);
    }

    /**
     * Set current error handler class.
     *
     * @param errorHandlerClass Error handler class.
     * @throws InjectException if unable to inject dependencies
     */
    public void setErrorHandler(Class<? extends ErrorHandler> errorHandlerClass) throws InjectException {
        this.errorHandler = dependencyManager.registerAndInject(errorHandlerClass);
    }

    /**
     * Initialization method.
     *
     * @throws Exception if any error occurred
     */
    public abstract void prepare() throws Exception;

    /**
     * Helper method that starts the application by given application class.
     *
     * @param appClass Aapplication class.
     */
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
