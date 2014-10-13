package com.sweetieframework.foundation.controllers;

import com.sweetieframework.foundation.Controller;
import com.sweetieframework.foundation.Request;
import com.sweetieframework.foundation.Response;


public class Default404NotFoundController extends Controller {

    public static Response process(Request request) {
        return ok("404 Not Found").setStatus(404);
    }

}
