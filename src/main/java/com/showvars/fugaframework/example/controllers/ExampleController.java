package com.showvars.fugaframework.example.controllers;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;
import java.nio.charset.Charset;

public class ExampleController extends Controller {
    
    public static Response skip(Context ctx) {
        ctx.getSession().put("Key", "MUHUHOHOHAHAHAHA");
        return proceed();
    }
    
    public static Response index(Context ctx, String one, int to) {
        return ok("Hello! " + one + " - " + to + " = " + ctx.getSession().getString("Key"));
    }
    
    public static Response post(Context ctx) {
        
        return ok(ctx.getRequest().getContent().toString(Charset.forName("UTF-8")));
    }
}
