package com.bunjlabs.fugaframework.foundation.controllers;

import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.Controller;
import com.bunjlabs.fugaframework.foundation.Response;
import com.bunjlabs.fugaframework.templates.TemplateNotFoundException;
import com.bunjlabs.fugaframework.templates.TemplateRenderException;
import com.bunjlabs.fugaframework.utils.MimeTypeUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DefaultController extends Controller {

    public Response generateNotFound() {
        String path = ctx.getApp().getConfiguration().get("fuga.404.redirect", null);
        if (path != null && !path.isEmpty()) {
            return temporaryRedirect(urls.that(path));
        }
        return notFound("404 Not Found");
    }

    public Response generateAsset(String path) {
        File asset;
        if (ctx.getApp().getConfiguration().getBoolean("fuga.resources.external", false)) {
            asset = new File(ctx.getApp().getConfiguration().get("fuga.resources.path", "./") + "/assets/" + path);
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

    public Response generateAssetView(String name) throws TemplateNotFoundException, TemplateRenderException {
        return ok(view(name));
    }

    public Response generateOk(String data) {

        return ok(data);
    }
}
