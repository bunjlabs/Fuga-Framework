package com.showvars.fugaframework.foundation;

import com.showvars.fugaframework.templates.TemplateNotFoundException;
import com.showvars.fugaframework.templates.TemplateRenderException;
import java.util.UUID;

public abstract class Controller extends Responses {

    protected static Response proceed() {
        return null;
    }

    protected static String view(Context ctx, String name, Object obj) throws TemplateNotFoundException, TemplateRenderException {
        return ctx.getApp().getTemplateEngine().renderToString(name, ctx, obj);
    }

    protected static String view(Context ctx, String name) throws TemplateNotFoundException, TemplateRenderException {
        return ctx.getApp().getTemplateEngine().renderToString(name, ctx);
    }

    protected static class Urls {

        public static String that(Context ctx, Object... args) {
            String path = "";
            if (args.length > 0 && args[0] != null && args[0] instanceof String) {
                path = (String) args[0];
            }
            StringBuilder sb = new StringBuilder();
            sb.append("http://")
                    .append(ctx.getRequest().getHost())
                    //.append(port)
                    .append("/")
                    .append(path);
            return sb.toString();
        }
    }

    protected static class Forms {

        public static String generateFormId(Context ctx, String formName) {
            String fid = UUID.randomUUID().toString();
            ctx.getSession().put("__formid__" + formName, fid);
            return fid;
        }

        public static boolean testFormId(Context ctx, String formName, String fid) {
            Object lastfid = ctx.getSession().get("__formid__" + formName);
            if (lastfid == null) {
                return false;
            }
            ctx.getSession().remove("__formid__" + formName);
            return fid.equals((String) lastfid);
        }
    }
}
