package com.sweetieframework.examples.helloworld.controllers;

import com.sweetieframework.foundation.Controller;
import com.sweetieframework.foundation.Response;
import com.sweetieframework.handlers.Mapped;
import io.netty.handler.codec.http.HttpRequest;



@Mapped(uri = "/")
public class HelloWorldIndexController extends Controller {

    @Override
    public Response process(HttpRequest request) {
        return ans("<h1>Hello World!</h1>").setContentType("text/html");
    }

}
