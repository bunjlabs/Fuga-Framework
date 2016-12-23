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
import com.bunjlabs.fuga.foundation.Result;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Router {

    private final Logger log = LogManager.getLogger(this);

    private final RouteMapLoader mapLoader;
    private final List<Extension> extensions = new ArrayList<>();

    /**
     * Create new router.
     *
     * @param app Fuga app.
     */
    public Router(FugaApp app) {
        mapLoader = new RouteMapLoader(app.getResourceManager().getResourceRepresenter("routes"));
    }

    /**
     * Load routes map file from the given path.
     *
     * @param path Path to the routes map file.
     */
    public void load(String path) {
        try {
            extensions.addAll(mapLoader.load(path));
            log.info("Routes loaded from: {}", path);
        } catch (RoutesMapLoadException | RoutesMapSyntaxException | FileNotFoundException ex) {
            log.error("unable to load routes map", ex);
        }
    }

    /**
     * Load routes map file from the given path in classpath.
     *
     * @param path Path to the routes map file.
     */
    public void loadFromClasspath(String path) {
        try {
            extensions.addAll(mapLoader.loadFromClasspath(path));
            log.info("Routes loaded from resources: {}", path);
        } catch (RoutesMapLoadException | RoutesMapSyntaxException | FileNotFoundException ex) {
            log.error("unable to load routes map", ex);
        }
    }

    /**
     * Load routes map from string.
     *
     * @param input Input routes map string.
     * @throws RoutesMapLoadException if is unable to load map
     * @throws RoutesMapSyntaxException if a syntax error in routes map
     */
    public void loadFromString(String input) throws RoutesMapLoadException, RoutesMapSyntaxException {
        extensions.addAll(mapLoader.loadFromString(input));
    }

    /**
     * Load routes map from input stream.
     *
     * @param input Input stream with routes map.
     * @throws RoutesMapLoadException if is unable to load map
     * @throws RoutesMapSyntaxException if a syntax error in routes map
     */
    public void load(InputStream input) throws RoutesMapLoadException, RoutesMapSyntaxException {
        extensions.addAll(mapLoader.load(input));
    }

    /**
     * Forward specified request context to the corresponding route and returns
     * generated response.
     *
     * @param ctx Request context.
     * @return generated result.
     */
    public Response forward(Context ctx) {
        if (extensions.isEmpty()) {
            Exception ex = new RoutesMapException("Empty routes map");
            log.catching(ex);
            return ctx.app().getErrorHandler().onServerError(ctx.request(), ex);
        }

        Result result;

        try {
            result = forward(ctx, ctx.request().path(), extensions);
        } catch (Exception ex) {
            log.catching(ex);
            return ctx.app().getErrorHandler().onServerError(ctx.request(), ex);
        }

        if (result == null) {
            return ctx.app().getErrorHandler().onClientError(ctx.request(), 404);
        }

        if (result.isEmpty() && result.status() >= 400 && result.status() < 500) {
            return ctx.app().getErrorHandler().onClientError(ctx.request(), result.status());
        }

        ctx.response().headers().putAll(result.headers());
        ctx.response()
                .status(result.status())
                .stream(result.stream())
                .length(result.length())
                .as(result.contentType());

        return ctx.response();
    }

    private Result forward(Context ctx, String path, List<Extension> exts) throws Exception {
        if (exts == null) {
            throw new RoutesMapException("Extensions sublist is null");
        }

        for (Extension ext : exts) {
            if (ext.getRequestMethods() != null
                    && !ext.getRequestMethods().isEmpty()
                    && !ext.getRequestMethods().contains(ctx.request().requestMethod())) {
                continue;
            }

            if (ext.getHost() != null
                    && !ext.getHost().matcher(ctx.request().host()).matches()) {
                continue;
            }

            Matcher m;
            boolean accumulate = false;
            if (ext.getPattern() == null) {
                m = null;
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
                Result result = invoke(ctx, route, args);
                if (result == null) {
                    throw new NullPointerException("Result is null in "
                            + route.getController().getName() + "."
                            + route.getMethod().toGenericString());
                }

                if (result.status() > 0) {
                    return result;
                }

            } else if (ext.getNodes() != null && !ext.getNodes().isEmpty()) {
                if (accumulate) {
                    path = path.substring(m.end());
                }
                Result result = forward(ctx, path, ext.getNodes());

                if (result != null && result.status() > 0) {
                    return result;
                }
            }
        }
        return null;
    }

    private Result invoke(Context ctx, Route route, Object... args) throws Exception {
        Controller controller = Controller.Builder.build(route.getController(), ctx);
        return (Result) route.getMethod().invoke(controller, args);
    }
}
