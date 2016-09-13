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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class ResourceManager {

    private static final String APP_DIR = "app";

    /**
     * Returns input stream of given resource from classpath.
     *
     * If specified resource does not exists, null value will be returned.
     *
     * @param path Path to the resource.
     * @return input stream of resource or null.
     * @throws FileNotFoundException
     */
    protected InputStream loadFromResources(String... path) throws FileNotFoundException {
        String name = String.join(File.separator, path);

        if (name == null || name.isEmpty()) {
            return null;
        }

        InputStream is;
        is = ResourceManager.class.getResourceAsStream(name.startsWith("/") ? name : ("/" + name));

        if (is == null) {
            throw new FileNotFoundException(name);
        }

        return is;
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
    protected InputStream load(String... path) throws FileNotFoundException {
        String name = String.join(File.separator, path);

        if (name == null || name.isEmpty()) {
            return null;
        }

        InputStream is;

        String resourcePathStr = "."
                + APP_DIR
                + (name.startsWith("/") ? name : (File.separator + name));

        try {
            is = new FileInputStream(resourcePathStr);
        } catch (FileNotFoundException ex) {
            return loadFromResources(name);
        }

        return is;
    }

    /**
     * Returns new resource representer for the given path.
     *
     * @param path Path to the represented resources.
     * @return resource representer for the given path.
     */
    public ResourceRepresenter getResourceRepresenter(String path) {
        return new ResourceRepresenter(this, path);
    }
}
