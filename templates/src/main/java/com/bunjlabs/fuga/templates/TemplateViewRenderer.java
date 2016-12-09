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
package com.bunjlabs.fuga.templates;

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.configuration.Configuration;
import com.bunjlabs.fuga.dependency.Inject;
import com.bunjlabs.fuga.foundation.Context;
import com.bunjlabs.fuga.views.ViewException;
import com.bunjlabs.fuga.views.ViewRenderer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class TemplateViewRenderer implements ViewRenderer {

    private final TemplateCompiler compiler;

    private final Map<String, Template> templates;
    private final Configuration config;

    /**
     *
     * @param app Current fuga app
     */
    @Inject
    public TemplateViewRenderer(FugaApp app) {
        this.compiler = new TemplateCompiler(app.getResourceManager().getResourceRepresenter("views"));
        this.config = app.getConfiguration();
        this.templates = new HashMap<>();
    }

    @Override
    public void render(String name, Context ctx, PrintStream output) throws ViewException {
        Template template = templates.get(name);

        if (template == null || config.getBoolean("fuga.templates.recompile")) {
            template = compiler.compile(name);
            templates.put(name, template);
        }

        template.render(ctx, output);
    }

    @Override
    public String renderToString(String name, Context ctx) throws ViewException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        render(name, ctx, new PrintStream(out));

        return out.toString();
    }

}
