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
package com.bunjlabs.fuga.foundation;

import com.bunjlabs.fuga.dependency.InjectException;
import com.bunjlabs.fuga.views.ViewException;
import java.lang.reflect.InvocationTargetException;

public abstract class Controller extends Results {

    /**
     * Context for the current request and application.
     */
    protected Context ctx;

    /**
     * Helper method that indicate that controller action returns nothing and
     * give controls to next action in routes map.
     *
     * @return null value.
     */
    protected final static Result proceed() {
        return new Result().status(-1);
    }

    /**
     * Render template by given name and returns result string.
     *
     * @param name Name of the template.
     * @return rendered template.
     * @throws ViewException if any error while rendering view
     */
    protected final String view(String name) throws ViewException {
        return ctx.app().getViewRenderer().renderToString(name, ctx);
    }

    /**
     * Controller builder
     */
    public static final class Builder {

        public static Controller build(Class<? extends Controller> controller, Context ctxb) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InjectException {
            Controller c = ctxb.app().getDependencyManager().inject(controller);

            c.ctx = ctxb;

            return c;
        }
    }
}
