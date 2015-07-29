package com.showvars.fugaframework.router;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.RequestMethod;
import com.showvars.fugaframework.foundation.Response;
import com.showvars.fugaframework.foundation.controllers.Default404NotFoundController;
import com.showvars.fugaframework.foundation.controllers.DefaultExceptionController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Router {

    private static final Logger log = LogManager.getLogger(Router.class);

    private final Map<RequestMethod, Map<Pattern, Route>> routes = new TreeMap<>();

    private final Pattern routeMapPattern = Pattern.compile(
            "^(GET|POST|PUT|DELETE|HEAD|OPTIONS)\\s+(\\S+)\\s+(\\w+(\\.\\w+)+)\\.(\\w+)\\(((\\d+\\:\\w+)(\\,\\s*(\\d+\\:\\w+))*)?\\)");

    public Router() {
        for (RequestMethod rm : RequestMethod.values()) {
            routes.put(rm, new HashMap<>());
        }
    }

    public void addRoute(RequestMethod requestMethod, Pattern pattern, Route route) {
        routes.get(requestMethod).put(pattern, route);
    }

    public void load(File file) {
        try {
            load(new FileInputStream(file));
            log.info("Routes loaded from: {}", file.getPath());
        } catch (FileNotFoundException ex) {
            log.catching(ex);
        }
    }

    public void load(String path) {
        try {
            load(new FileInputStream(path));
            log.info("Routes loaded from: {}", path);
        } catch (FileNotFoundException ex) {
            log.catching(ex);
        }
    }

    public void loadFromResources(String path) {
        try {
            load(Router.class.getResourceAsStream(path));
            log.info("Routes loaded from resources: {}", path);
        } catch (NullPointerException ex) {
            log.catching(ex);
        }
    }

    public void load(InputStream input) throws NullPointerException {
        if (input == null) {
            throw new NullPointerException();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(input));

        br.lines().forEach((String line) -> {
            Matcher m;
            if ((m = routeMapPattern.matcher(line)).matches()) {
                try {
                    String[] parameters = m.group(6) == null ? new String[0] : m.group(6).replaceAll("\\s", "").split("\\,");
                    List<MethodParameter> paramlist = new ArrayList<>();
                    Class[] classes = new Class[parameters.length + 1];
                    classes[0] = Context.class;
                    for (int i = 0; i < parameters.length; i++) {
                        String[] ss = parameters[i].split(":");
                        classes[i + 1] = getClassTypeBySimpleName(ss[1]);
                        paramlist.add(new MethodParameter(Integer.parseInt(ss[0]), ss[1]));

                    }

                    addRoute(RequestMethod.valueOf(m.group(1)),
                            Pattern.compile(m.group(2)),
                            new Route(Class.forName(m.group(3)).getMethod(m.group(5), classes), paramlist));

                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
                    log.catching(ex);
                }
            } else if (line.trim().length() <= 0 || line.trim().startsWith("#")) {
                // Nothing...
            } else {
                log.error("Syntax error in {}", line);
            }
        });

    }

    public Response forward(Context ctx) {
        Matcher m;
        for (Map.Entry<Pattern, Route> e : routes.get(ctx.getRequest().getRequestMethod()).entrySet()) {
            if ((m = e.getKey().matcher(ctx.getRequest().getPath())).matches()) {
                try {
                    Object[] args = new Object[e.getValue().getParameters().size() + 1];
                    args[0] = ctx;
                    for (int i = 1; i < args.length; i++) {
                        MethodParameter mp = e.getValue().getParameters().get(i - 1);
                        args[i] = mp.cast(m.group(mp.getCaptureGroup()));
                    }

                    return (Response) e.getValue().getMethod().invoke(null, args);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    ex.getCause().printStackTrace();
                    Throwable cause = ex.getCause();
                    if (cause == null) {
                        return DefaultExceptionController.process(ctx, ex);
                    } else if (cause instanceof Exception) {
                        return DefaultExceptionController.process(ctx, (Exception) cause);
                    } else {
                        return DefaultExceptionController.process(ctx, ex);
                    }
                }
            }
        }

        return Default404NotFoundController.process(ctx);
    }

    private static Class getClassTypeBySimpleName(String name) throws ClassNotFoundException {
        switch (name) {
            case "String":
            case "string":
                return String.class;
            case "int":
            case "integer":
                return int.class;
            case "long":
                return long.class;
            case "short":
                return short.class;
            case "byte":
                return byte.class;
            case "char":
                return char.class;
            case "boolean":
                return boolean.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            default:
                throw new ClassNotFoundException(name);

        }
    }
}
