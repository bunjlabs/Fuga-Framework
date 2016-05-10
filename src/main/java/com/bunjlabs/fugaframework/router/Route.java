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
package com.bunjlabs.fugaframework.router;

import com.bunjlabs.fugaframework.foundation.Controller;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Route {

    private final Class cls;
    private final Method method;
    private final List<RouteParameter> parameters;

    public Route(Class<? extends Controller> cls, Method method, List<RouteParameter> parameters) {
        this.cls = cls;
        this.method = method;
        this.parameters = new ArrayList<>();
        if (parameters != null) {
            this.parameters.addAll(parameters);
        }
    }

    public Method getMethod() {
        return method;
    }

    public Class<? extends Controller> getController() {
        return cls;
    }

    public List<RouteParameter> getParameters() {
        return parameters;
    }
}
