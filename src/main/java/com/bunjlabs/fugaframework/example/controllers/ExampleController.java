package com.bunjlabs.fugaframework.example.controllers;

import com.bunjlabs.fugaframework.dependency.Inject;
import com.bunjlabs.fugaframework.example.ExampleDependency;
import com.bunjlabs.fugaframework.example.FugaExampleApp;
import com.bunjlabs.fugaframework.example.services.ExampleService;
import com.bunjlabs.fugaframework.foundation.Controller;
import com.bunjlabs.fugaframework.foundation.Response;
import com.bunjlabs.fugaframework.templates.TemplateNotFoundException;
import com.bunjlabs.fugaframework.templates.TemplateRenderException;
import java.nio.charset.Charset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleController extends Controller {

    private static final Logger log = LogManager.getLogger(ExampleController.class);

    @Inject
    public FugaExampleApp app;

    @Inject
    public ExampleDependency ed;

    @Inject
    public ExampleService es;

    public Response skip() {
        ctx.getSession().put("Key", "Some important string");

        ctx.put("key", "Context value");

        return proceed();
    }

    public Response index(String one, int to) throws TemplateNotFoundException, TemplateRenderException {
        log.info(ed.getString());

        ctx.put("data",
                "Hello! " + one + " - " + to + " = " + ctx.getSession().getString("Key")
                + " | " + ctx.get("key", String.class));

        return ok(view("defaults/example.html"));
    }

    public Response post() {
        return ok(ctx.getRequest().getContent().toString(Charset.forName("UTF-8")));
    }

    public Response noData() {
        return ok();
    }
}
