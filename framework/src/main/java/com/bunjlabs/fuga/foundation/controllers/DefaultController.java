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
package com.bunjlabs.fuga.foundation.controllers;

import com.bunjlabs.fuga.configuration.Configuration;
import com.bunjlabs.fuga.dependency.Inject;
import com.bunjlabs.fuga.foundation.Controller;
import com.bunjlabs.fuga.foundation.Result;
import com.bunjlabs.fuga.resources.ResourceManager;
import com.bunjlabs.fuga.resources.ResourceRepresenter;
import com.bunjlabs.fuga.views.ViewException;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DefaultController extends Controller {

    private final Configuration conf;
    private final ResourceRepresenter resourceRepresenter;

    @Inject
    public DefaultController(Configuration conf, ResourceManager resourceManager) {
        this.conf = conf;
        this.resourceRepresenter = resourceManager.getResourceRepresenter("assets");
    }

    public Result generateNotFound() {
        return notFound();
    }

    public Result generateSeeOther(String url) {
        return seeOther(ctx.urls().that(url));
    }

    public Result generateAsset(String path) {
        InputStream asset;
        try {
            asset = resourceRepresenter.load(path);
        } catch (FileNotFoundException ex) {
            return notFound();
        }

        String mime = conf.get("fuga.mimetype." + path.substring(path.lastIndexOf('.') + 1), "application/octet-stream");

        return ok(asset).as(mime);
    }

    public Result generateView(String name) throws ViewException {
        return ok(view(name));
    }

    public Result generateOk(String data) {

        return ok(data);
    }
}
