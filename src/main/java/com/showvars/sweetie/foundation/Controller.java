package com.showvars.sweetie.foundation;

import com.showvars.sweetie.foundation.controllers.Default404NotFoundController;
import com.showvars.sweetie.templates.TemplateEngine;
import com.showvars.sweetie.templates.TemplateNotFoundException;
import com.showvars.sweetie.templates.TemplateRenderException;
import java.io.File;
import java.io.InputStream;
import java.util.UUID;

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
    
    protected static Response notFoundDefault(Context ctx) {
        return Default404NotFoundController.process(ctx);
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
    
    protected static class Urls {
        
        public static String that(Context ctx, Object... args) {
            String path = "";
            if (args.length > 0 && args[0] != null && args[0] instanceof String) {
                path = (String) args[0];
            }
            StringBuilder sb = new StringBuilder();
            sb.append("http://")
                    .append(ctx.getRequest().getHost())
                    //.append(port)
                    .append("/")
                    .append(path);
            return sb.toString();
        }
    }
    
    protected static class Forms {
        
        public static String generateFormId(Context ctx, String formName) {
            String fid = UUID.randomUUID().toString();
            ctx.getSession().put("__formid__" + formName, fid);
            return fid;
        }
        
        public static boolean testFormId(Context ctx, String formName, String fid) {
            Object lastfid = ctx.getSession().get("__formid__" + formName);
            if (lastfid == null) {
                return false;
            }
            ctx.getSession().remove("__formid__" + formName);
            return fid.equals((String) lastfid);
        }
    }
}
