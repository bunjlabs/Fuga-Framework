package com.bunjlabs.fugaframework.foundation;

import com.bunjlabs.fugaframework.FugaApp;
import com.bunjlabs.fugaframework.dependency.InjectException;
import com.bunjlabs.fugaframework.templates.TemplateNotFoundException;
import com.bunjlabs.fugaframework.templates.TemplateRenderException;
import java.lang.reflect.InvocationTargetException;

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

        public static Controller build(Class<? extends Controller> controller, FugaApp app, Context ctxb) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InjectException {
            Controller c = controller.getConstructor().newInstance();
            c.ctx = ctxb;
            c.urls = new Urls(ctxb);
            c.forms = new Forms(ctxb);

            app.getDependencyManager().injectDependencies(c);

            return c;
        }
    }
}
