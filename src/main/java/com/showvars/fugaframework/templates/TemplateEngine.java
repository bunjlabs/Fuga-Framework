package com.showvars.fugaframework.templates;

import com.showvars.fugaframework.FugaApp;
import com.showvars.fugaframework.configuration.Configuration;
import com.showvars.fugaframework.foundation.Context;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    private final FugaApp app;
    private final Configuration config;

    public TemplateEngine(FugaApp app) {
        this.app = app;
        config = app.getConfiguration();
    }

    public String compile(String name) throws TemplateNotFoundException, TemplateRenderException {
        if (templates.containsKey(name) && !config.getBoolean("fuga.templates.alwaysrecompile", false)) {
            return templates.get(name).getTid();
        }

        InputStream is;
        try {
            if (config.getBoolean("fuga.resources.external", false)) {
                is = new FileInputStream(config.get("fuga.resources.path", ".") + File.separator + "views" + File.separator + name);
            } else {
                is = TemplateEngine.class.getResourceAsStream("/views/" + name);
            }
        } catch (IOException ex) {
            throw new TemplateNotFoundException("Unable to load: " + name);
        }
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
        String tid
                = config.getBoolean("fuga.templates.alwaysrecompile", false)
                && templates.containsKey(name)
                        ? templates.get(name).getTid() : Template.generateTid();
        Template t = new Template(this, tid, input.toString());
        t.compile(engine);
        templates.put(name, t);

        return tid;
    }

    public void render(String name, PrintStream output, Context ctx, Object obj) throws TemplateNotFoundException, TemplateRenderException {
        String tid = compile(name);
        try {
            Invocable inv = (Invocable) engine;
            inv.invokeFunction("process_" + tid, output, ctx, obj, new TemplateApi(app, ctx));
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

}
