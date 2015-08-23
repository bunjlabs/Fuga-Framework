package com.showvars.fugaframework.router;

import com.showvars.fugaframework.foundation.RequestMethod;
import java.util.List;
import java.util.regex.Pattern;

public class Extension {

    private List<Extension> nodes = null;
    private Route route = null;

    private final RequestMethod requestMethod;
    private final Pattern pattern;

    Extension(RequestMethod requestMethod, Pattern pattern) {
        this.requestMethod = requestMethod;
        this.pattern = pattern;
    }

    Extension(RequestMethod requestMethod, Pattern pattern, List<Extension> nodes) {
        this.requestMethod = requestMethod;
        this.pattern = pattern;
        this.nodes = nodes;
    }

    Extension(RequestMethod requestMethod, Pattern pattern, Route route) {
        this.requestMethod = requestMethod;
        this.pattern = pattern;
        this.route = route;
    }

    public List<Extension> getNodes() {
        return nodes;
    }

    public Route getRoute() {
        return route;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
