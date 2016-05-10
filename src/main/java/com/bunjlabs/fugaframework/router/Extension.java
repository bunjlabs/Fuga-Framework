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

import com.bunjlabs.fugaframework.foundation.RequestMethod;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Extension {

    private List<Extension> nodes = null;
    private Route route = null;

    private final Set<RequestMethod> requestMethods;
    private final Pattern pattern;

    Extension(Set<RequestMethod> requestMethods, Pattern pattern) {
        this.requestMethods = requestMethods;
        this.pattern = pattern;
    }

    Extension(Set<RequestMethod> requestMethods, Pattern pattern, List<Extension> nodes) {
        this.requestMethods = requestMethods;
        this.pattern = pattern;
        this.nodes = nodes;
    }

    Extension(Set<RequestMethod> requestMethods, Pattern pattern, Route route) {
        this.requestMethods = requestMethods;
        this.pattern = pattern;
        this.route = route;
    }

    public List<Extension> getNodes() {
        return nodes;
    }

    public Route getRoute() {
        return route;
    }

    public Set<RequestMethod> getRequestMethods() {
        return requestMethods;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
