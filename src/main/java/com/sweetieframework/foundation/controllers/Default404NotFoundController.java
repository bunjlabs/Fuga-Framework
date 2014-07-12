package com.sweetieframework.foundation.controllers;

import com.sweetieframework.foundation.Controller;
import com.sweetieframework.foundation.Request;
import com.sweetieframework.foundation.Response;
import java.util.List;


public class Default404NotFoundController extends Controller {

    @Override
    public Response process(Request request, List<String> matches) {
        return ans("404 Not Found").setStatus(404);
    }

}
