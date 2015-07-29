package com.showvars.fugaframework.foundation.controllers;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;
import com.showvars.fugaframework.utils.MimeTypeUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class DefaultAssetsController extends Controller {

    public static Response get(Context ctx, String path) {
        File asset;
        if (ctx.getApp().getConfiguration().getBoolean("fuga.resources.external", false)) {
            asset = new File(ctx.getApp().getConfiguration().get("fuga.resources.path", ".") + "/assets/" + path);
        } else {
            asset = new File(DefaultAssetsController.class.getResource("/assets/" + path).getFile());
        }
        if(!asset.isFile() || !asset.exists() || !asset.canRead()) {
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

}
