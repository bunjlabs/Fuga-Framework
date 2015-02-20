package com.showvars.sweetie.templates;

import com.showvars.sweetie.foundation.Context;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TemplateEngine {

    private static final Pattern codePattern = Pattern.compile("(\\{%([\\s\\S]*?)%\\})|(\\{#([^\\n\\r]*?)#\\})");

    private final Map<String, String> templates = new HashMap<>();
    private final TemplateApi api;

    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    public TemplateEngine(TemplateApi api) {
        this.api = api;
        engine.put("api", api);
    }

    //public String compile(String input) throws TemplateRenderException {
    //    return new Template(this, input).compile(engine);
    //}
    public String compile(String name) throws TemplateNotFoundException, TemplateRenderException {
        String tid;
        if ((tid = templates.get(name)) == null) {
            InputStream is = TemplateEngine.class.getResourceAsStream("/views/" + name);

            if (is == null) {
                throw new TemplateNotFoundException("Enable to read resource: /views/" + name);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder input = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    input.append(line).append("\n");
                }
            } catch (IOException ex) {
                throw new TemplateNotFoundException("Enable to read resource: /views/" + name);
            }
            tid = new Template(this, input.toString()).compile(engine); //compile(input.toString());
            templates.put(name, tid);
        }
        return tid;
    }

    public String getTid(String name) {
        return templates.get(name);
    }

    public void render(String name, PrintStream output, Context ctx, Object obj) throws TemplateNotFoundException, TemplateRenderException {
        String tid = compile(name);
        try {
            Invocable inv = (Invocable) engine;
            inv.invokeFunction("process_" + tid, output, ctx, obj);
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
