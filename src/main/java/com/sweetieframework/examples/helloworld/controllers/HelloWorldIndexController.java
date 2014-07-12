package com.sweetieframework.examples.helloworld.controllers;

import com.sweetieframework.foundation.Controller;
import com.sweetieframework.foundation.Request;
import com.sweetieframework.foundation.Response;
import com.sweetieframework.handlers.Mapped;
import java.util.List;
import java.util.Map;

@Mapped(pattern = "/(.*)")
public class HelloWorldIndexController extends Controller {

    @Override
    public Response process(Request request, List<String> matches) {
        StringBuilder matchesString = new StringBuilder();
        for (String match : matches) {
            matchesString.append(match).append("\n");
        }

        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : request.getParams().entrySet()) {
            paramString.append(entry.getKey()).append(" = ");
            String key = entry.getKey();
            for (String value : entry.getValue()) {
                paramString.append(" [").append(value).append("] ");
            }
            paramString.append("\n");
        }
        return ans("<h1>Hello World!</h1><br/>"
                + "<h3>Matching groups</h3><pre>" + matchesString.toString() + "</pre>"
                + "<h3>GET params</h3><pre>" + paramString.toString() + "</pre>")
                .setContentType("text/html");
    }

}
