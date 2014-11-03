package com.showvars.sweetie.foundation.controllers;

import com.showvars.sweetie.foundation.Context;
import com.showvars.sweetie.foundation.Controller;
import com.showvars.sweetie.foundation.Response;

public class Default404NotFoundController extends Controller {

    public static Response process(Context ctx) {
        return notFound("404 Not Found");
    }

}
