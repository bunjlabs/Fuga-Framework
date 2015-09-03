package com.showvars.fugaframework.foundation.controllers;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;
import com.showvars.fugaframework.templates.TemplateNotFoundException;
import com.showvars.fugaframework.templates.TemplateRenderException;
import com.showvars.fugaframework.utils.MimeTypeUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DefaultController extends Controller {

    public static Response notFound(Context ctx) {
        String path = ctx.getApp().getConfiguration().get("fuga.404.redirect", null);
        if (path != null) {
            return temporaryRedirect(Controller.Urls.that(ctx, path));
        }
        return notFound("404 Not Found");
    }

    public static Response asset(Context ctx, String path) {
        File asset;
        if (ctx.getApp().getConfiguration().getBoolean("fuga.resources.external", false)) {
            asset = new File(ctx.getApp().getConfiguration().get("fuga.resources.path", ".") + "/assets/" + path);
        } else {
            asset = new File(DefaultController.class.getResource("/assets/" + path).getFile());
        }
        if (!asset.isFile() || !asset.exists() || !asset.canRead()) {
            return notFound();
        }
        String mime = MimeTypeUtils.getMimeTypeByExt(asset.getName().substring(asset.getName().lastIndexOf('.') + 1));
        try {
            return ok(new FileInputStream(asset))
                    .setContentLength(asset.length())
                    .setContentType(mime != null ? mime : "application/octet-stream");
        } catch (IOException ex) {
            return notFound();
        }
    }

    public static Response exception(Context ctx, Exception e) {
        StackTraceElement[] ste = e.getStackTrace();

        StringBuilder sb = new StringBuilder();

        for (StackTraceElement el : ste) {
            sb.append(el.toString()).append("\n");
        }
        return ok("<code>" + e.toString() + "</code><br><pre>" + sb.toString() + "</pre>");
    }
    
    public static Response viewTemplate(Context ctx, String name) throws TemplateNotFoundException, TemplateRenderException {
        return ok(view(ctx, name));
    }
}
