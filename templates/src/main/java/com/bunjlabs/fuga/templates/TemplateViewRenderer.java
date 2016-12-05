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

/**
 *
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TemplateViewRenderer implements ViewRenderer {

    private final TemplateCompiler compiler;

    private final Map<String, Template> templates;
    private final Configuration config;

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
