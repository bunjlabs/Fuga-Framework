package com.showvars.fugaframework.example.controllers;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;

public class ExampleController extends Controller {
    
    public static Response skip(Context ctx) {
        ctx.getSession().put("Key", "MUHUHOHOHAHAHAHA");
        return proceed();
    }
    
    public static Response index(Context ctx, String one, int to) {
        return ok("Hello! " + one + " - " + to + " = " + ctx.getSession().getString("Key"));
    }
}
