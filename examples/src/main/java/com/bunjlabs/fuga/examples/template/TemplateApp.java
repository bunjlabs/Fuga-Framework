package com.bunjlabs.fuga.examples.template;

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.templates.TemplateViewRenderer;

public class TemplateApp extends FugaApp {

    @Override
    public void prepare() throws Exception {
        getRouter().loadFromString("view(\"template.html\")");
        getConfiguration().set("fuga.templates.recompile", "true");

        setViewRenderer(TemplateViewRenderer.class);
    }

    public static void main(String[] args) throws Exception {
        launch(TemplateApp.class);
    }
}
