package com.sweetieframework.examples.helloworld.controllers;

import com.sweetieframework.foundation.Controller;
import com.sweetieframework.foundation.Request;
import com.sweetieframework.foundation.Response;
import java.util.List;
import java.util.Map;

public class HelloWorldIndexController extends Controller {

    public static Response index(Request request, String name) {
        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : request.getParameters().entrySet()) {
            paramString.append(entry.getKey()).append(" = ");
            String key = entry.getKey();
            entry.getValue().stream().forEach((value) -> {
                paramString.append(" [").append(value).append("] ");
            });
            paramString.append("\n");
        }
        return ok("<h1>Hello World!</h1><br/>"
                + "<h3>Matched group</h3><pre>" + name + "</pre>"
                + "<h3>GET params</h3><pre>" + paramString.toString() + "</pre>")
                .setContentType("text/html");
    }

}
