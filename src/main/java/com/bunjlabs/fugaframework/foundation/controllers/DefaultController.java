package com.bunjlabs.fugaframework.foundation.controllers;

import com.bunjlabs.fugaframework.foundation.Controller;
import com.bunjlabs.fugaframework.foundation.Response;
import com.bunjlabs.fugaframework.templates.TemplateNotFoundException;
import com.bunjlabs.fugaframework.templates.TemplateRenderException;
import com.bunjlabs.fugaframework.utils.MimeTypeUtils;
import java.io.InputStream;

public class DefaultController extends Controller {

    public Response generateNotFound() {
        return notFound();
    }

    public Response generateAsset(String path) {
        InputStream asset = ctx.getApp().getResourceManager().load("assets/" + path);

        if (asset == null) {
            return notFound();
        }

        String mime = MimeTypeUtils.getMimeTypeByExt(path.substring(path.lastIndexOf('.') + 1));

        return ok(asset).as(mime != null ? mime : "application/octet-stream");
    }

    public Response generateAssetView(String name) throws TemplateNotFoundException, TemplateRenderException {
        return ok(view(name));
    }

    public Response generateOk(String data) {

        return ok(data);
    }
}
