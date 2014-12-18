package com.showvars.sweetie.foundation;

import com.showvars.sweetie.templates.TemplateEngine;
import com.showvars.sweetie.templates.TemplateNotFoundException;
import com.showvars.sweetie.templates.TemplateRenderException;
import java.io.File;
import java.io.InputStream;

public abstract class Controller {

    protected static Response notFound() {
        return new Response().setStatus(404);
    }

    protected static Response notFound(String s) {
        return new Response(s).setStatus(404);
    }

    protected static Response notFound(File f) {
        return new Response(f).setStatus(404);
    }

    protected static Response notFound(InputStream is) {
        return new Response(is).setStatus(404);
    }

    protected static Response ok(String s) {
        return new Response(s);
    }

    protected static Response ok(File f) {
        return new Response(f);
    }

    protected static Response ok(InputStream is) {
        return new Response(is);
    }

    protected static Response nothing() {
        return new Response().setStatus(204);
    }

    protected static Response redirect(String url) {
        return new Response().setStatus(301).setHeader("Location", url);
    }

    protected static Response temporaryRedirect(String url) {
        return new Response().setStatus(302).setHeader("Location", url);
    }

    protected static String view(Context ctx, String name, Object obj) throws TemplateNotFoundException, TemplateRenderException {
        return ctx.getApp().getTemplateEngine().renderToString(name, ctx, obj);
    }

    protected static String view(Context ctx, String name) throws TemplateNotFoundException, TemplateRenderException {
        return ctx.getApp().getTemplateEngine().renderToString(name, ctx);
    }
}
