package com.bunjlabs.fugaframework.foundation;

import com.bunjlabs.fugaframework.templates.TemplateNotFoundException;
import com.bunjlabs.fugaframework.templates.TemplateRenderException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public abstract class Controller extends Responses {

    protected Context ctx;
    protected Urls urls;
    protected Forms forms;

    protected static Response proceed() {
        return null;
    }

    protected String view(String name, Object data) throws TemplateNotFoundException, TemplateRenderException {
        return ctx.getApp().getTemplateEngine().renderToString(name, ctx, data);
    }

    protected String view(String name) throws TemplateNotFoundException, TemplateRenderException {
        return ctx.getApp().getTemplateEngine().renderToString(name, ctx);
    }

    public static class Builder {

        public static Controller build(Class controller, Context ctxb) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Controller c = (Controller) controller.getConstructor().newInstance();
            c.ctx = ctxb;
            c.urls = new Urls(c);
            c.forms = new Forms(c);
            return c;
        }
    }

    protected static class Urls {

        private final Controller controller;

        private Urls(Controller controller) {
            this.controller = controller;
        }

        public String that(Object... args) {
            String path = "";
            if (args.length > 0 && args[0] != null && args[0] instanceof String) {
                path = (String) args[0];
            }
            StringBuilder sb = new StringBuilder();
            sb.append("http://")
                    .append(controller.ctx.getRequest().getHost())
                    .append("/")
                    .append(path);
            return sb.toString();
        }
    }

    protected static class Forms {

        private final Controller controller;

        private Forms(Controller controller) {
            this.controller = controller;
        }

        public String generateFormId(String formName) {
            String fid = UUID.randomUUID().toString();
            controller.ctx.getSession().put("__formid__" + formName, fid);
            return fid;
        }

        public boolean testFormId(String formName, String fid) {
            Object lastfid = controller.ctx.getSession().get("__formid__" + formName);
            if (lastfid == null) {
                return false;
            }
            controller.ctx.getSession().remove("__formid__" + formName);
            return fid.equals((String) lastfid);
        }
    }

}
