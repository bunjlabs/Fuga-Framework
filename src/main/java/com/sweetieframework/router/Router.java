package com.sweetieframework.router;

import com.sweetieframework.foundation.Request;
import com.sweetieframework.foundation.RequestMethod;
import com.sweetieframework.foundation.RequestMethodUtil;
import com.sweetieframework.foundation.Response;
import com.sweetieframework.foundation.controllers.Default404NotFoundController;
import com.sweetieframework.foundation.controllers.DefaultExceptionController;
import com.sweetieframework.network.HttpServer;
import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {

    private static final Logger log = Logger.getLogger(HttpServer.class.getName());

    private final Map<RequestMethod, Map<Pattern, Route>> routes = new TreeMap<>();

    private final Pattern routeMapPattern = Pattern.compile(
            "^(GET|POST|PUT|DELETE|HEAD)\\s+(\\S+)\\s+(\\w+(\\.\\w+)+)\\.(\\w+)\\(((\\d+\\:\\w+)(\\,\\s*(\\d+\\:\\w+))*)?\\)");

    public Router() {
        for (RequestMethod rm : RequestMethod.values()) {
            routes.put(rm, new HashMap<>());
        }
    }

    public void addRoute(RequestMethod requestMethod, Pattern pattern, Route route) {
        routes.get(requestMethod).put(pattern, route);
    }

    public void load(Reader r) {
        BufferedReader br = new BufferedReader(r);

        br.lines().forEach((String line) -> {
            Matcher m;
            if ((m = routeMapPattern.matcher(line)).matches()) {
                try {
                    String[] parameters = m.group(6) == null ? new String[0] : m.group(6).replaceAll("\\s", "").split("\\,");
                    List<MethodParameter> paramlist = new ArrayList<>();
                    Class[] classes = new Class[parameters.length + 1];
                    classes[0] = Request.class;
                    for (int i = 0; i < parameters.length; i++) {
                        String[] ss = parameters[i].split(":");
                        classes[i + 1] = getClassTypeBySimpleName(ss[1]);
                        paramlist.add(new MethodParameter(Integer.parseInt(ss[0]), ss[1]));

                    }

                    addRoute(RequestMethodUtil.valueOf(m.group(1)),
                            Pattern.compile(m.group(2)),
                            new Route(Class.forName(m.group(3)).getMethod(m.group(5), classes), paramlist));

                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            } else {
                log.log(Level.SEVERE, "Syntax error in \"{0}\"", line);
            }
        });

    }

    public Response forward(Request request) {
        Matcher m;
        for (Map.Entry<Pattern, Route> e : routes.get(request.getRequestMethod()).entrySet()) {
            if ((m = e.getKey().matcher(request.getPath())).matches()) {
                try {
                    Object[] args = new Object[e.getValue().getParameters().size() + 1];
                    args[0] = request;
                    for(int i = 1; i < args.length; i++) {
                        MethodParameter mp = e.getValue().getParameters().get(i - 1);
                        args[i] = mp.cast(m.group(mp.getCaptureGroup()));
                    }
                    
                    return (Response) e.getValue().getMethod().invoke(null, args);
                } catch (Exception ex) {
                    return DefaultExceptionController.process(request, ex);
                }
            }
        }

        return Default404NotFoundController.process(request);
    }

    private static Class getClassTypeBySimpleName(String name) throws ClassNotFoundException {
        switch (name) {
            case "String":
                return String.class;
            case "int":
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
