package com.bunjlabs.fugaframework.router;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Route {
    private final Class cls;
    private final Method method;
    private final List<RouteParameter> parameters;

    public Route(Class cls, Method method, List<RouteParameter> parameters) {
        this.cls = cls;
        this.method = method;
        this.parameters = new ArrayList<>();
        if (parameters != null) {
            this.parameters.addAll(parameters);
        }
    }

    public Method getMethod() {
        return method;
    }
    
    public Class getController() {
        return cls;
    }

    public List<RouteParameter> getParameters() {
        return parameters;
    }
}
