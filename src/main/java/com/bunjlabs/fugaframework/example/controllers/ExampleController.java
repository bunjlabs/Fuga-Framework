package com.bunjlabs.fugaframework.example.controllers;

import com.bunjlabs.fugaframework.foundation.Controller;
import com.bunjlabs.fugaframework.foundation.Response;
import com.bunjlabs.fugaframework.templates.TemplateNotFoundException;
import com.bunjlabs.fugaframework.templates.TemplateRenderException;
import java.nio.charset.Charset;

public class ExampleController extends Controller {

    public Response skip() {
        ctx.getSession().put("Key", "Some important string");

        ctx.put("key", "Context value");
        
        return proceed();
    }

    public Response index(String one, int to) throws TemplateNotFoundException, TemplateRenderException {
        ctx.put("data", "Hello! " + one + " - " + to + " = " + ctx.getSession().getString("Key")
                + " | " + ctx.get("key", String.class));

        return ok(view("defaults/example.html"));
    }

    public Response post() {
        return ok(ctx.getRequest().getContent().toString(Charset.forName("UTF-8")));
    }
}
