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
package com.bunjlabs.fuga.handlers;

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.dependency.Inject;
import com.bunjlabs.fuga.foundation.Context;
import com.bunjlabs.fuga.foundation.Request;
import com.bunjlabs.fuga.foundation.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultRequestHandler implements RequestHandler {

    private final Logger log = LogManager.getLogger(DefaultRequestHandler.class);

    private final FugaApp app;

    @Inject
    public DefaultRequestHandler(FugaApp app) {
        this.app = app;
    }

    @Override
    public Response onRequest(Request request) {
        Context ctx = new Context(request, app);

        log.debug("Access from {}: {}{}", request.getRemoteAddress(), request.getHost(), request.getUri());

        return app.getRouter().forward(ctx);
    }

}
