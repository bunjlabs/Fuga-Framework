/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fugaframework.example.controllers;

import com.bunjlabs.fugaframework.dependency.Inject;
import com.bunjlabs.fugaframework.example.ExampleDependency;
import com.bunjlabs.fugaframework.example.FugaExampleApp;
import com.bunjlabs.fugaframework.example.services.ExampleService;
import com.bunjlabs.fugaframework.foundation.Controller;
import com.bunjlabs.fugaframework.foundation.Request;
import com.bunjlabs.fugaframework.foundation.Response;
import com.bunjlabs.fugaframework.foundation.Responses;
import com.bunjlabs.fugaframework.handlers.RequestHandler;
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

    @Inject
    public RequestHandler re;

    public Response skip() {
        ctx.getSession().put("Key", "Some important string");

        ctx.put("key", "Context value");

        return proceed();
    }

    public Response index(String one, int to) throws TemplateNotFoundException, TemplateRenderException {
        log.info(ed.getString());

        ctx.put("data",
                "Hello! " + one + " - " + to + " = " + ctx.getSession().get("Key")
                + " | " + ctx.get("key", String.class));

        return ok(view("defaults/example.html"));
    }

    public Response post() {
        return ok(ctx.getRequest().getContent().toString(Charset.forName("UTF-8")));
    }

    public Response throwError() {
        int a = 1 / 0;
        return ok("WiGO?");
    }

    public Response nf() {
        return notFound();
    }

    public Response showCurrentRequestHandler() {
        return ok("Current request handler is: " + re.getClass().getName());
    }

    public Response changeDefaultRequestHandler() {
        app.setRequestHandler((Request request) -> Responses.ok("I'm dummy request handler!"));

        return ok("Done!");
    }

    public Response noData() {
        return ok();
    }
}
