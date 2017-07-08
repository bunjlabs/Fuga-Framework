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
package com.bunjlabs.fuga.dependency;

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.foundation.Context;
import com.bunjlabs.fuga.foundation.Controller;
import com.bunjlabs.fuga.foundation.Response;
import com.bunjlabs.fuga.foundation.Result;
import com.bunjlabs.fuga.router.Extension;
import com.bunjlabs.fuga.router.Route;
import com.bunjlabs.fuga.router.RouteMapLoader;
import com.bunjlabs.fuga.router.RouteParameter;
import com.bunjlabs.fuga.router.RoutesMapException;
import com.bunjlabs.fuga.router.RoutesMapLoadException;
import com.bunjlabs.fuga.router.RoutesMapSyntaxException;

import java.io.FileNotFoundException;
import java.io.InputStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DependencyManager {

    private final Logger log = LogManager.getLogger(DependencyManager.class);

    private final Map<Class, Object> dependencies = new HashMap<>();

    /**
     * Create new dependency manager.
     */
    public DependencyManager() {
    }

    /**
     * Creates new object from specified class and inject dependences to
     * contructor and public fields.
     *
     * @param <T> Type of injectable object.
     * @param injectable Injectable class.
     * @return object with ijected dependences.
     * @throws InjectException if is unable to iject dependences
     */
    public <T> T inject(Class<T> injectable) throws InjectException {
        Constructor<T> annotatedConstructor = getAnnotatedConstructor(injectable);

        if (annotatedConstructor != null) {
            return injectToConstructor(annotatedConstructor);
        }

        T obj;
        try {
            obj = injectable.getConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new InjectException("Unable to instatiate class by default constructor of " + injectable.getName(), e);
        }

        injectToFields(obj, getAnnotatedFields(injectable));

        return obj;
    }

    private <T> T injectToConstructor(Constructor<T> injactableConstructor) throws InjectException {
        Object[] parameters = new Object[injactableConstructor.getParameterCount()];
        Class[] parameterTypes = injactableConstructor.getParameterTypes();

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = getDependency(parameterTypes[i]);
        }
        try {
            return injactableConstructor.newInstance(parameters);
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new InjectException("Unable to inject by constructor", ex);
        }
    }

    private void injectToFields(Object injectable, List<Field> injectableFields) throws InjectException {
        for (Field f : injectableFields) {
            injectField(injectable, f);
        }
    }

    private void injectField(Object injectable, Field injectableField) throws InjectException {
        try {
            Class cls = injectableField.getType();
            Object injectOnject = getDependency(cls);

            injectableField.set(injectable, injectOnject);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new InjectException("Unable to inject field", e);
        }

    }

    private Constructor getAnnotatedConstructor(Class injactable) {
        return Stream.of(injactable.getConstructors())
                .filter((Constructor c) -> c.isAnnotationPresent(Inject.class))
                .findFirst().orElse(null);
    }

    private List<Field> getAnnotatedFields(Class injectable) {
        return Stream.of(injectable.getFields())
                .filter((Field f) -> f.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
    }

    private Object getDependency(Class cls) throws InjectException {
        if (dependencies.containsKey(cls)) {
            return getDependency(cls, dependencies.get(cls));
        } else {
            for (Map.Entry<Class, Object> e : dependencies.entrySet()) {
                if (cls.isAssignableFrom(e.getKey())) {
                    return getDependency(cls, e.getValue());
                }
            }
        }

        throw new InjectException("No suitable dependency for " + cls.getName());
    }

    private Object getDependency(Class cls, Object obj) throws InjectException {
        if (obj != null) {
            return obj;
        }

        return inject(cls);
    }

    /**
     * Register specified class and object as dependency.
     *
     * @param cls Dependency class.
     * @param obj Dependency object.
     */
    public void register(Class cls, Object obj) {
        dependencies.put(cls, obj);
    }

    /**
     * Register specified objects as dependencies.
     *
     * @param objs Dependency objects.
     */
    public void register(Object... objs) {
        for (Object obj : objs) {
            register(obj.getClass(), obj);
        }
    }

    /**
     * Register specified classes as dependencies.
     *
     * @param clss Dependency classess.
     */
    public void register(Class... clss) {
        for (Class cls : clss) {
            register(cls, null);
        }
    }

    /**
     * Creates new object from specified class, inject dependences to contructor
     * and public fields and register this object as dependency.
     *
     * Calling this method is identical to code:
     * <pre>
     * T obj = inject(injectable);
     * register(injectable, obj);
     * </pre>
     *
     * @param <T> Type of injectable object.
     * @param injectable Injectable class.
     * @return injected object.
     * @throws InjectException if is unable to iject dependences
     */
    public <T> T registerAndInject(Class<T> injectable) throws InjectException {
        T obj = inject(injectable);
        register(injectable, obj);
        return obj;
    }

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
                return ctx.app()
                          .getErrorHandler()
                          .onServerError(ctx.request(), ex);
            }

            Result result;

            try {
                result = forward(ctx, ctx.request().path(), extensions);
            } catch (Exception ex) {
                log.catching(ex);
                return ctx.app()
                          .getErrorHandler()
                          .onServerError(ctx.request(), ex);
            }

            if (result == null) {
                return ctx.app()
                          .getErrorHandler()
                          .onClientError(ctx.request(), 404);
            }

            if (result.isEmpty() && result.status() >= 400 && result.status() < 500) {
                return ctx.app()
                          .getErrorHandler()
                          .onClientError(ctx.request(), result.status());
            }

            ctx.response()
               .headers()
               .putAll(result.headers());
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
                if (ext.getRequestMethods() != null && !ext.getRequestMethods().isEmpty() &&
                    !ext.getRequestMethods().contains(ctx.request().requestMethod())) {
                    continue;
                }

                if (ext.getHost() != null && !ext.getHost()
                                                 .matcher(ctx.request().host())
                                                 .matches()) {
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
                        throw new NullPointerException("Result is null in " + route.getController().getName() + "." +
                                                       route.getMethod().toGenericString());
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
}
