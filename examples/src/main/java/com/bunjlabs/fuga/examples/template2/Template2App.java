package com.bunjlabs.fuga.examples.template2;

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.templates.TemplateViewRenderer;

/**
 *
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class Template2App extends FugaApp {

    @Override
    public void prepare() throws Exception {
        getRouter().load("template2.froutes");

        setViewRenderer(TemplateViewRenderer.class);
    }

    public static void main(String[] args) throws Exception {
        launch(Template2App.class);
    }
}
