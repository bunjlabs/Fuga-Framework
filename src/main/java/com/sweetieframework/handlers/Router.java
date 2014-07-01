package com.sweetieframework.handlers;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Router {

    private final Map<Pattern, Handler> handlers = new HashMap<>();

    public void register(Handler h) {
        Annotation annotation = h.getClass().getAnnotation(Mapped.class);
        if (annotation != null) {
            handlers.put(Pattern.compile(((Mapped) annotation).uri()), h);
        }
    }

    public Handler getMatchedByUri(String uri) {
        for (Map.Entry<Pattern, Handler> e : handlers.entrySet()) {
            if (e.getKey().matcher(uri).matches()) {
                return e.getValue();
            }
        }
        return null;
    }
}