package com.bunjlabs.fugaframework.router;

import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.Controller;
import com.bunjlabs.fugaframework.foundation.Response;
import com.bunjlabs.fugaframework.foundation.Responses;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Router {

    private static final Logger log = LogManager.getLogger(Router.class);

    private List<Extension> extensions = null;

    public void load(File file) {
        try {
            load(new FileInputStream(file));
            log.info("Routes loaded from: {}", file.getPath());
        } catch (FileNotFoundException | NullPointerException | RoutesMapLoadException | RoutesMapSyntaxException ex) {
            log.catching(ex);
        }
    }

    public void load(String path) {
        try {
            load(new FileInputStream(path));
            log.info("Routes loaded from: {}", path);
        } catch (FileNotFoundException | NullPointerException | RoutesMapLoadException | RoutesMapSyntaxException ex) {
            log.catching(ex);
        }
    }

    public void loadFromResources(String path) {
        try {
            load(Router.class.getResourceAsStream("/" + path));
            log.info("Routes loaded from resources: {}", path);
        } catch (NullPointerException | RoutesMapLoadException | RoutesMapSyntaxException ex) {
            log.catching(ex);
        }
    }

    public void load(InputStream input) throws NullPointerException, RoutesMapLoadException, RoutesMapSyntaxException {
        if (input == null) {
            throw new NullPointerException();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(input));

        RouteMapLoader mapLoader = new RouteMapLoader();

        extensions = mapLoader.load(input);

    }

    public Response forward(Context ctx) {
        if (extensions == null || extensions.isEmpty()) {
            return Responses.internalServerError(new RoutesMapException("Empty routes map"));
        }

        Response resp;

        try {
            resp = forward(ctx, extensions);
        } catch (RoutesMapException ex) {
            return Responses.internalServerError();
        }

        if (resp != null) {
            return resp;
        }

        return Responses.notFound("404 Not Found");
    }

    private Response forward(Context ctx, List<Extension> exts) throws RoutesMapException {
        if (exts == null) {
            throw new RoutesMapException("Extensions sublist is null");
        }
        Matcher m;
        for (Extension ext : exts) {
            if (ext.getRequestMethod() != null && ext.getRequestMethod() != ctx.getRequest().getRequestMethod()) {
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

                try {
                    Response resp = invoke(ctx, route, args);
                    if (resp != null) {
                        return resp;
                    }
                } catch (Exception ex) {
                    log.catching(ex);
                    return Responses.internalServerError(ex);
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

    private static Response invoke(Context ctx, Route route, Object... args) throws Exception {
        Controller controller = Controller.Builder.build(route.getController(), ctx);
        return (Response) route.getMethod().invoke(controller, args);
    }
}
