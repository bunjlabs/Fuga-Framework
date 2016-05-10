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
package com.bunjlabs.fugaframework.foundation.controllers;

import com.bunjlabs.fugaframework.configuration.Configuration;
import com.bunjlabs.fugaframework.dependency.Inject;
import com.bunjlabs.fugaframework.foundation.Controller;
import com.bunjlabs.fugaframework.foundation.Response;
import com.bunjlabs.fugaframework.templates.TemplateNotFoundException;
import com.bunjlabs.fugaframework.templates.TemplateRenderException;
import java.io.InputStream;

public class DefaultController extends Controller {

    private final Configuration conf;

    @Inject
    public DefaultController(Configuration conf) {
        this.conf = conf;
    }

    public Response generateNotFound() {
        return notFound();
    }

    public Response generateAsset(String path) {
        InputStream asset = ctx.getApp().getResourceManager().load("assets/" + path);

        if (asset == null) {
            return notFound();
        }

        String mime = conf.get("fuga.mimetype." + path.substring(path.lastIndexOf('.') + 1), "application/octet-stream");

        return ok(asset).as(mime);
    }

    public Response generateAssetView(String name) throws TemplateNotFoundException, TemplateRenderException {
        return ok(view(name));
    }

    public Response generateOk(String data) {

        return ok(data);
    }
}
