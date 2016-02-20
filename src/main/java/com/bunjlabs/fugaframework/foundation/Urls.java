package com.bunjlabs.fugaframework.foundation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Urls {

    private final Context ctx;

    public Urls(Context ctx) {
        this.ctx = ctx;
    }

    public String that(Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://") // TODO!
                .append(ctx.getRequest().getHost());

        if (args.length <= 0) {
            sb.append("/");
        } else {
            for (Object arg : args) {
                if (arg != null) {
                    sb.append('/').append(arg);
                }
            }
        }

        return sb.toString();
    }

    public String asset(Object... args) {
        StringBuilder sb = new StringBuilder();

        if (args.length <= 0) {
            sb.append("/");
        } else {
            for (Object arg : args) {
                if (arg != null) {
                    sb.append('/').append(arg);
                }
            }
        }

        if (!ctx.getApp().getConfiguration().getBoolean("fuga.assets.external")) {
            return that("assets" + sb.toString());
        } else {
            return ctx.getApp().getConfiguration().get("fuga.assets.path") + sb.toString();
        }
    }
    
    
    public String urlencode(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        try {
            return URLEncoder.encode(sb.toString(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return "";
        }
    }
}
