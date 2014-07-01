package com.sweetieframework.examples.helloworld.controllers;

import com.sweetieframework.foundation.Controller;
import com.sweetieframework.handlers.Mapped;
import io.netty.handler.codec.http.HttpRequest;



@Mapped(uri = "/")
public class HelloWorldHandler extends Controller {

    @Override
    public void process(HttpRequest request, StringBuilder buff) {
        buff.append("Hello World!");
    }

}
