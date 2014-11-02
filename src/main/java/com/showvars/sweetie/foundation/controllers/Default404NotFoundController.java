package com.showvars.sweetie.foundation.controllers;

import com.showvars.sweetie.foundation.Controller;
import com.showvars.sweetie.foundation.Request;
import com.showvars.sweetie.foundation.Response;


public class Default404NotFoundController extends Controller {

    public static Response process(Request request) {
        return ok("404 Not Found").setStatus(404);
    }

}
