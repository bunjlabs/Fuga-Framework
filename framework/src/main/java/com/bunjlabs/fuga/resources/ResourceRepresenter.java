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
package com.bunjlabs.fuga.resources;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceRepresenter {

    private final ResourceManager resourceManager;
    private final String base;

    /**
     * Create new resource representer for the specified resource manager and
     * base path.
     *
     * @param resourceManager Resource manager
     * @param base Base path.
     */
    protected ResourceRepresenter(ResourceManager resourceManager, String base) {
        this.resourceManager = resourceManager;
        this.base = base;

    }

    /**
     * Returns input stream of given resource from classpath.
     *
     * If specified resource does not exists, null value will be returned.
     *
     * @param path Path to the resource.
     * @return input stream of resource or null.
     * @throws FileNotFoundException
     */
    public InputStream loadFromResources(String path) throws FileNotFoundException {
        return resourceManager.loadFromResources(base, path);
    }

    /**
     * Returns input stream of given resource.
     *
     * This method at first find resource in current <code>app</code> dir. If it
     * fails, it try to find resource from class path. If specified resource
     * does not exists, null value will be returned.
     *
     * @param path Path to the resource.
     * @return input stream of resource or null.
     * @throws FileNotFoundException
     */
    public InputStream load(String path) throws FileNotFoundException {
        return resourceManager.load(base, path);
    }
}
