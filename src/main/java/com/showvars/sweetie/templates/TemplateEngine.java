package com.showvars.sweetie.templates;

import com.showvars.sweetie.foundation.Context;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class TemplateEngine {

    private final Map<String, Template> templates = new HashMap<>();

    public void render(String name, PrintStream output, Context ctx, Object obj) throws TemplateNotFoundException, TemplateRenderException {
        Template tpl;
        if ((tpl = templates.get(name)) == null) {
            tpl = new Template();
            InputStream is = TemplateEngine.class.getResourceAsStream("/" + name);
            if (is == null) {
                throw new TemplateNotFoundException();
            }
            tpl.compile(new InputStreamReader(is));
            templates.put(name, tpl);
        }
        tpl.render(output, ctx, obj);

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

    private static TemplateEngine instance;

    public static TemplateEngine getInstance() {
        if (instance == null) {
            instance = new TemplateEngine();
        }
        return instance;
    }
}
