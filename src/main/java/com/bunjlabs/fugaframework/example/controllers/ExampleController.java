package com.bunjlabs.fugaframework.example.controllers;


import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.Controller;
import com.bunjlabs.fugaframework.foundation.Response;
import java.nio.charset.Charset;

public class ExampleController extends Controller {
    
    public static Response skip(Context ctx) {
        ctx.getSession().put("Key", "Some important string");
        
        ctx.put("key", "Context value");
        return proceed();
    }
    
    public static Response index(Context ctx, String one, int to) {
        return ok("Hello! " + one + " - " + to + " = " + ctx.getSession().getString("Key") +
                " | " + ctx.get("key", String.class));
    }
    
    public static Response post(Context ctx) {
        
        return ok(ctx.getRequest().getContent().toString(Charset.forName("UTF-8")));
    }
}
