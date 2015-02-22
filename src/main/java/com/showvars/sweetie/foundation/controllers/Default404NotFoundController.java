package com.showvars.sweetie.foundation.controllers;

import com.showvars.sweetie.foundation.Context;
import com.showvars.sweetie.foundation.Controller;
import com.showvars.sweetie.foundation.Response;

public class Default404NotFoundController extends Controller {

    public static Response process(Context ctx) {
        String path = ctx.getApp().getConfiguration().get("sweetie.404.redirect", null);
        if (path != null) {
            return temporaryRedirect(Urls.that(ctx, path));
        }
        return notFound("404 Not Found");
    }

}
