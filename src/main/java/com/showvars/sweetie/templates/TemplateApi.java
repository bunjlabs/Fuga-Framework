package com.showvars.sweetie.templates;

import com.showvars.sweetie.SweetieApp;
import com.showvars.sweetie.foundation.Context;
import java.nio.charset.Charset;
import org.apache.commons.lang3.StringEscapeUtils;

public class TemplateApi {

    private final SweetieApp app;
    private final Context ctx;

    public TemplateApi(SweetieApp app, Context ctx) {
        this.app = app;
        this.ctx = ctx;

    }

    public String asset(Object... args) {
        String path = "";
        if (args.length > 0 && args[0] != null && args[0] instanceof String) {
            path = (String) args[0];
        }
        if (!app.getConfiguration().getBoolean("sweetie.templates.assets.external", false)) {
            return that("assets/" + path);
        } else {
            return app.getConfiguration().get("sweetie.templates.assets.path", that()) + path;
        }
    }

    public String that(Object... args) {
        String path = "";
        if (args.length > 0 && args[0] != null && args[0] instanceof String) {
            path = (String) args[0];
        }
        //String port = app.getConfiguration().getInt("sweetie.http.bindport", 8080) == 80
        //        ? "" : ":" + app.getConfiguration().getInt("sweetie.http.bindport", 8080);
        StringBuilder sb = new StringBuilder();
        sb.append("http://")
                .append(ctx.getRequest().getHost())
                //.append(port)
                .append("/")
                .append(path);
        return sb.toString();
    }

    public byte[] bytes(Object... args) {
        String str = "";
        if (args.length > 0 && args[0] != null && args[0] instanceof String) {
            str = (String) args[0];
        }

        return str.getBytes(Charset.forName("UTF-8"));
    }

    public String escape(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object a : args) {
            sb.append(a != null ? a : "");
        }

        return StringEscapeUtils.escapeHtml4(sb.toString());
    }
}
