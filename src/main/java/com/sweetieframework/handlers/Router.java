package com.sweetieframework.handlers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {

    private final Map<Pattern, Handler> handlers = new HashMap<>();

    public void register(Handler h) {
        Annotation annotation = h.getClass().getAnnotation(Mapped.class);
        if (annotation != null) {
            handlers.put(Pattern.compile(((Mapped) annotation).pattern()), h);
        }
    }

    public HandlerMatched getMatchedByUri(String pattern) {
        Matcher m;
        for (Map.Entry<Pattern, Handler> e : handlers.entrySet()) {
            if ((m = e.getKey().matcher(pattern)).matches()) {
                ArrayList<String> matches = new ArrayList<>();
                for (int i = 0; i <= m.groupCount(); i++) {
                    matches.add(m.group(i));
                }
                return new HandlerMatched(e.getValue(), matches);
            }
        }
        return null;
    }
}
