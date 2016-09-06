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
package com.bunjlabs.fuga.router;

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.foundation.Context;
import com.bunjlabs.fuga.foundation.Controller;
import com.bunjlabs.fuga.foundation.Response;
import com.bunjlabs.fuga.foundation.Responses;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Router {

    private static final Logger log = LogManager.getLogger(Router.class);

    private final RouteMapLoader mapLoader;
    private final List<Extension> extensions = new ArrayList<>();

    public Router(FugaApp app) {
        mapLoader = new RouteMapLoader(app.getResourceManager().getResourceRepresenter(app.getConfiguration().get("fuga.dirs.routes")));
    }

    public void load(String path) {
        try {
            extensions.addAll(mapLoader.load(path));
            log.info("Routes loaded from: {}", path);
        } catch (RoutesMapLoadException | RoutesMapSyntaxException | FileNotFoundException ex) {
            log.error("unable to load routes map", ex);
        }
    }

    public void loadFromResources(String path) {
        try {
            extensions.addAll(mapLoader.loadFromResources(path));
            log.info("Routes loaded from resources: {}", path);
        } catch (RoutesMapLoadException | RoutesMapSyntaxException | FileNotFoundException ex) {
            log.error("unable to load routes map", ex);
        }
    }

    public void loadFromString(String input) throws NullPointerException, RoutesMapLoadException, RoutesMapSyntaxException {
        extensions.addAll(mapLoader.loadFromString(input));
    }

    private void load(InputStream input) throws NullPointerException, RoutesMapLoadException, RoutesMapSyntaxException {
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
            resp = forward(ctx, ctx.getRequest().getPath(), extensions);
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

    private Response forward(Context ctx, String path, List<Extension> exts) throws Exception {
        if (exts == null) {
            throw new RoutesMapException("Extensions sublist is null");
        }

        for (Extension ext : exts) {
            if (ext.getRequestMethods() != null
                    && !ext.getRequestMethods().isEmpty()
                    && !ext.getRequestMethods().contains(ctx.getRequest().getRequestMethod())) {
                continue;
            }

            Matcher m;
            boolean accumulate = false;
            if (ext.getPattern() == null) {
                m = null;
                //} else if (!(m = ext.getPattern().matcher(ctx.getRequest().getPath())).matches()) {
                //    continue;
            } else {
                m = ext.getPattern().matcher(path);

                boolean cont;
                if (ext.isPatternAccumulator()) {
                    accumulate = cont = m.lookingAt();
                } else {
                    cont = m.matches();
                }

                if (!cont) {
                    continue;
                }
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
                if (accumulate) {
                    path = path.substring(m.end());
                }
                Response resp = (Response) forward(ctx, path, ext.getNodes());
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
