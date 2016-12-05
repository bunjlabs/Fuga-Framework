package com.bunjlabs.fuga.templates;

import com.bunjlabs.fuga.foundation.Context;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.apache.commons.lang3.StringEscapeUtils;

public class TemplateApi {

    public final Context ctx;

    public TemplateApi(Context ctx) {
        this.ctx = ctx;
    }

    public String that(Object... args) {
        return ctx.urls().that(args);
    }

    public String asset(Object... args) {
        return ctx.urls().asset(args);
    }

    public String urlencode(Object... args) {
        return ctx.urls().urlencode(args);
    }

    public String generateFormId(Object... args) {
        if (args.length < 1 || args[0] == null) {
            return "";
        }
        return ctx.forms().generateFormId(args[0].toString());
    }

    public boolean testFormId(Object... args) {
        if (args.length < 2 || args[0] == null || args[1] == null) {
            return false;
        }
        return ctx.forms().testFormId(args[0].toString(), args[1].toString());
    }

    public String msg(Object... args) {
        if (args.length == 0) {
            return "";
        }

        if (args.length == 1) {
            return args[0] != null ? ctx.msg().get(args[0].toString()) : "";
        }

        return ctx.msg().get(args[0].toString(), Arrays.copyOfRange(args, 1, args.length));
    }

    public String combine(Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                sb.append(arg);
            }
        }

        return sb.toString();
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
