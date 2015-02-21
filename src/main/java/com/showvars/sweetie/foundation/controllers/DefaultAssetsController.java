package com.showvars.sweetie.foundation.controllers;

import com.showvars.sweetie.foundation.Context;
import com.showvars.sweetie.foundation.Controller;
import com.showvars.sweetie.foundation.Response;
import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class DefaultAssetsController extends Controller {

    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
    }
    
    public static Response get(Context ctx, String path) {
        URL u = DefaultAssetsController.class.getResource("/assets/" + path);
        if (u == null) {
            return Default404NotFoundController.process(ctx);
        }

        File asset = new File(DefaultAssetsController.class.getResource("/assets/" + path).getFile());
        Object[] mimes = MimeUtil.getMimeTypes(asset).toArray();
        try {
            return ok(new FileInputStream(asset))
                    .setContentLength(asset.length())
                    .setContentType(mimes.length > 0 ? mimes[0].toString() : "application/octet-stream");
        } catch (IOException ex) {
            return Default404NotFoundController.process(ctx);
        }
    }

}
