/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fugaframework.foundation;

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

        public static Controller build(Class<? extends Controller> controller, Context ctxb) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InjectException {
            Controller c = controller.getConstructor().newInstance();
            c.ctx = ctxb;
            c.urls = new Urls(ctxb);
            c.forms = new Forms(ctxb);

            ctxb.getApp().getDependencyManager().injectDependencies(c);

            return c;
        }
    }
}
