package com.sweetieframework.examples.helloworld.controllers;

import com.sweetieframework.foundation.Controller;
import com.sweetieframework.foundation.Response;
import com.sweetieframework.handlers.Mapped;
import io.netty.handler.codec.http.HttpRequest;
import java.util.List;



@Mapped(pattern = "/(\\d+)")
public class HelloWorldIndexController extends Controller {

    @Override
    public Response process(HttpRequest request, List<String> matches) {
        StringBuilder matchesString = new StringBuilder();
        for(String match : matches){
            matchesString.append(match).append("\n");
        }
        return ans("<h1>Hello World!</h1><br/><pre>" + matchesString.toString() + "</pre>").setContentType("text/html");
    }

}
