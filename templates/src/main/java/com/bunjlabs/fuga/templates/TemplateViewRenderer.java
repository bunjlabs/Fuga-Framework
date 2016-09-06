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
import com.bunjlabs.fuga.dependency.Inject;
import com.bunjlabs.fuga.foundation.Context;
import com.bunjlabs.fuga.views.ViewRenderer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class TemplateViewRenderer implements ViewRenderer {

    private final TemplateEngine templateEngine;

    @Inject
    public TemplateViewRenderer(FugaApp app) {
        templateEngine = new TemplateEngine(app);
    }

    @Override
    public void render(String name, Context ctx, PrintStream output) throws TemplateNotFoundException, TemplateRenderException {
        templateEngine.render(name, ctx, output);
    }

    @Override
    public String renderToString(String name, Context ctx) throws TemplateRenderException, TemplateNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        render(name, ctx, new PrintStream(out));

        try {
            return out.toString("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new TemplateRenderException(ex);
        }
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
