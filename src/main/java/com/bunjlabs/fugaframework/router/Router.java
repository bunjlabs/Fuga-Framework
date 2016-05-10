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
package com.bunjlabs.fugaframework.router;

import com.bunjlabs.fugaframework.FugaApp;
import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.Controller;
import com.bunjlabs.fugaframework.foundation.Response;
import com.bunjlabs.fugaframework.foundation.Responses;
import com.bunjlabs.fugaframework.resources.ResourceManager;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Router {

    private static final Logger log = LogManager.getLogger(Router.class);

    private final ResourceManager resourceManager;
    private final List<Extension> extensions = new ArrayList<>();

    public Router(FugaApp app) {
        this.resourceManager = app.getResourceManager();
    }

    public void load(String path) {
        try {
            load(resourceManager.load(path));
            log.info("Routes loaded from: {}", path);
        } catch (NullPointerException | RoutesMapLoadException | RoutesMapSyntaxException ex) {
            log.error("Unable to load routes from: {}", path, ex);
        }
    }

    public void loadFromResources(String path) {
        try {
            load(resourceManager.loadFromResources(path));
            log.info("Routes loaded from resources: {}", path);
        } catch (NullPointerException | RoutesMapLoadException | RoutesMapSyntaxException ex) {
            log.error("Unable to load routes from: {}", path, ex);
        }
    }

    public void loadFromString(String input) throws NullPointerException, RoutesMapLoadException, RoutesMapSyntaxException {
        load(new ByteArrayInputStream(input.getBytes()));
    }

    private void load(InputStream input) throws NullPointerException, RoutesMapLoadException, RoutesMapSyntaxException {
        if (input == null) {
            throw new NullPointerException();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(input));

        RouteMapLoader mapLoader = new RouteMapLoader();

        extensions.addAll(mapLoader.load(input));
    }

    public Response forward(Context ctx) {
        if (extensions.isEmpty()) {
            Exception ex = new RoutesMapException("Empty routes map");
            log.catching(ex);
            return Responses.internalServerError(ex);
        }

        Response resp;

        try {
            resp = forward(ctx, extensions);
        } catch (Exception ex) {
            log.catching(ex);
            return ctx.getApp().getErrorHandler().onServerError(ctx.getRequest(), ex);
        }

        if (resp == null) {
            resp = ctx.getApp().getErrorHandler().onClientError(ctx.getRequest(), 404);
        }

        if (resp.isEmpty() && resp.getStatus() >= 400 && resp.getStatus() < 500) {
            resp = ctx.getApp().getErrorHandler().onClientError(ctx.getRequest(), resp.getStatus());
        }

        return resp;
    }

    private Response forward(Context ctx, List<Extension> exts) throws Exception {
        if (exts == null) {
            throw new RoutesMapException("Extensions sublist is null");
        }
        Matcher m;
        for (Extension ext : exts) {
            if (ext.getRequestMethods() != null && !ext.getRequestMethods().isEmpty() && !ext.getRequestMethods().contains(ctx.getRequest().getRequestMethod())) {
                continue;
            }
            if (ext.getPattern() == null) {
                m = null;
            } else if (!(m = ext.getPattern().matcher(ctx.getRequest().getPath())).matches()) {
                continue;
            }

            if (ext.getRoute() != null) {
                Route route = ext.getRoute();
                Object[] args = new Object[route.getParameters().size()];

                for (int i = 0; i < args.length; i++) {
                    RouteParameter mp = route.getParameters().get(i);
                    if (mp.getType() == RouteParameter.ParameterType.CAPTURE_GROUP) {
                        args[i] = mp.cast(m.group(mp.getCaptureGroup()));
                    } else {
                        args[i] = mp.cast();
                    }
                }
                Response resp = invoke(ctx, route, args);
                if (resp != null) {
                    return resp;
                }
            } else if (ext.getNodes() != null && !ext.getNodes().isEmpty()) {
                Response resp = (Response) forward(ctx, ext.getNodes());
                if (resp != null) {
                    return resp;
                }
            }
        }
        return null;
    }

    private Response invoke(Context ctx, Route route, Object... args) throws Exception {
        Controller controller = Controller.Builder.build(route.getController(), ctx);
        return (Response) route.getMethod().invoke(controller, args);
    }
}
