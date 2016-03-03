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
package com.bunjlabs.fugaframework.templates;

import com.bunjlabs.fugaframework.FugaApp;
import com.bunjlabs.fugaframework.configuration.Configuration;
import com.bunjlabs.fugaframework.foundation.Context;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TemplateEngine {

    private final Map<String, Template> templates = new HashMap<>();
    private final ScriptEngine engine;

    private final FugaApp app;
    private final Configuration config;

    public TemplateEngine(FugaApp app) {
        this.app = app;
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        config = app == null ? null : app.getConfiguration();
    }

    public String compile(String name) throws TemplateNotFoundException, TemplateRenderException {
        if (templates.containsKey(name) && !config.getBoolean("fuga.templates.recompile")) {
            return templates.get(name).getTid();
        }

        InputStream is = app.getResourceManager().load("views" + (name.startsWith("/") ? name : ("/" + name)));

        if (is == null) {
            throw new TemplateNotFoundException("Unable to load: " + name);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder input = new StringBuilder();

        String line;

        try {
            while ((line = reader.readLine()) != null) {
                input.append(line).append("\n");
            }
        } catch (IOException ex) {
            throw new TemplateNotFoundException("Unable to load: " + name);
        }

        return compile(name, input.toString());
    }

    public String compile(String name, String input) throws TemplateRenderException, TemplateNotFoundException {
        String tid
                = config.getBoolean("fuga.templates.recompile")
                && templates.containsKey(name)
                        ? templates.get(name).getTid() : Template.generateTid();

        Template t = new Template(this, tid, input);
        t.compile();

        templates.put(name, t);

        t.eval(engine);
        
        return tid;
    }

    public void render(String name, PrintStream output, Context ctx, Object obj) throws TemplateNotFoundException, TemplateRenderException {
        String tid = compile(name);

        try {
            Invocable inv = (Invocable) engine;
            inv.invokeFunction("process_" + tid, output, ctx, obj, new TemplateApi(ctx));
        } catch (ScriptException | NoSuchMethodException ex) {
            throw new TemplateRenderException(ex.getLocalizedMessage());
        }
    }

    public void render(String name, Context ctx, PrintStream output) throws TemplateNotFoundException, TemplateRenderException {
        render(name, output, ctx, null);
    }

    public String renderToString(String name, Context ctx, Object obj) throws TemplateNotFoundException, TemplateRenderException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        render(name, new PrintStream(out), ctx, obj);

        return out.toString();
    }

    public String renderToString(String name, Context ctx) throws TemplateNotFoundException, TemplateRenderException {
        return renderToString(name, ctx, null);
    }

    public String getJsSource(String name) {
        return templates.get(name).getJsSource();
    }

}
