package com.showvars.sweetie.foundation.controllers;

import com.showvars.sweetie.foundation.Context;
import com.showvars.sweetie.foundation.Controller;
import com.showvars.sweetie.foundation.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class DefaultAssetsController extends Controller {

    public static Response get(Context ctx, String path) {
        URL u = DefaultAssetsController.class.getResource("/assets/" + path);
        if (u == null) {
            return Default404NotFoundController.process(ctx);
        }

        File asset = new File(DefaultAssetsController.class.getResource("/assets/" + path).getFile());

        try {
            return ok(new FileInputStream(asset))
                    .setContentLength(asset.length())
                    .setContentType(Files.probeContentType(asset.toPath()));
        } catch (IOException ex) {
            return Default404NotFoundController.process(ctx);
        }
    }

}
