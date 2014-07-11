package com.sweetieframework.foundation.controllers;

import com.sweetieframework.foundation.Controller;
import com.sweetieframework.foundation.Response;
import io.netty.handler.codec.http.HttpRequest;
import java.util.List;


public class Default404NotFoundController extends Controller {

    @Override
    public Response process(HttpRequest request, List<String> matches) {
        return ans("404 Not Found").setStatus(404);
    }

}
