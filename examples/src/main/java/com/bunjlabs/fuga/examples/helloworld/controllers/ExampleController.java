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
package com.bunjlabs.fuga.examples.helloworld.controllers;

import com.bunjlabs.fuga.dependency.Inject;
import com.bunjlabs.fuga.examples.helloworld.ExampleDependency;
import com.bunjlabs.fuga.examples.helloworld.FugaExampleApp;
import com.bunjlabs.fuga.examples.helloworld.services.ExampleService;
import com.bunjlabs.fuga.foundation.Controller;
import com.bunjlabs.fuga.foundation.Request;
import com.bunjlabs.fuga.foundation.Response;
import com.bunjlabs.fuga.foundation.Responses;
import com.bunjlabs.fuga.handlers.RequestHandler;
import com.bunjlabs.fuga.views.ViewException;
import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleController extends Controller {

    private static final Logger log = LogManager.getLogger(ExampleController.class);

    public final FugaExampleApp app;
    public final ExampleDependency ed;
    public final ExampleService es;
    public final RequestHandler re;

    @Inject
    public ExampleController(FugaExampleApp app, ExampleDependency ed, ExampleService es, RequestHandler re) {
        this.app = app;
        this.ed = ed;
        this.es = es;
        this.re = re;
    }

    public Response skip() {
        ctx.getSession().put("Key", "Some important string");

        ctx.put("key", "Context value");

        return proceed();
    }

    public Response index(String one, int to) throws ViewException {
        log.info(ed.getString());

        ctx.put("data",
                "Hello! " + one + " - " + to + " = " + ctx.getSession().get("Key")
                + " | " + ctx.get("key", String.class));

        return ok(view("defaults/example.html"));
    }

    public Response postString() {
        return ok(ctx.getRequest().getContent().asString()).asText();
    }

    public Response postStream() {
        return ok(ctx.getRequest().getContent().asInputStream());
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

    public Response file() throws IOException {
        return ok(new File("/home/show/Symfony_book_3.0.pdf"));
    }

    public Response noData() {
        return ok();
    }
}
