package com.showvars.fugaframework.foundation.controllers;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;

public class Default404NotFoundController extends Controller {

    public static Response process(Context ctx) {
        String path = ctx.getApp().getConfiguration().get("fuga.404.redirect", null);
        if (path != null) {
            return temporaryRedirect(Urls.that(ctx, path));
        }
        return notFound("404 Not Found");
    }

}
