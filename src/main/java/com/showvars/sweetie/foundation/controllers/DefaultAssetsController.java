package com.showvars.sweetie.foundation.controllers;

import com.showvars.sweetie.foundation.Context;
import com.showvars.sweetie.foundation.Controller;
import com.showvars.sweetie.foundation.Response;
import com.showvars.sweetie.utils.MimeTypeUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class DefaultAssetsController extends Controller {

    public static Response get(Context ctx, String path) {
        File asset;
        if (ctx.getApp().getConfiguration().getBoolean("sweetie.resources.external", false)) {
            asset = new File(ctx.getApp().getConfiguration().get("sweetie.resources.path", ".") + "/assets/" + path);
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
