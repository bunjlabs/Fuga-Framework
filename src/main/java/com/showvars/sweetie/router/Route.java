package com.showvars.sweetie.router;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Route {

    private final Method method;
    private final List<MethodParameter> parameters;

    public Route(Method method, List<MethodParameter> parameters) {
        this.method = method;
        this.parameters = new ArrayList<>();
        if (parameters != null) {
            this.parameters.addAll(parameters);
        }
    }

    public Method getMethod() {
        return method;
    }

    public List<MethodParameter> getParameters() {
        return parameters;
    }
}