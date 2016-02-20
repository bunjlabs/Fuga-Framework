package com.bunjlabs.fugaframework.templates;

import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.Forms;
import com.bunjlabs.fugaframework.foundation.Urls;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.apache.commons.lang3.StringEscapeUtils;

public class TemplateApi {

    private final Context ctx;

    public final Urls urls;
    public final Forms forms;

    public TemplateApi(Context ctx) {
        this.ctx = ctx;
        this.urls = new Urls(ctx);
        this.forms = new Forms(ctx);
    }

    public byte[] bytes(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return sb.toString().getBytes(Charset.forName("UTF-8"));
    }

    public String escape(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return StringEscapeUtils.escapeHtml4(sb.toString());
    }

    public String nltobr(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return sb.toString().replaceAll("\n", "<br>");
    }

    public String format(Object... args) {
        if (args.length == 0) {
            return "";
        }
        if (args.length == 1) {
            return args[0] != null ? args[0].toString() : "";
        }

        return String.format(args[0].toString(), Arrays.copyOfRange(args, 1, args.length));
    }

}
