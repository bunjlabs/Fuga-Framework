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
package com.bunjlabs.fugaframework.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class ResourceManager {

    protected InputStream loadFromResources(String... path) {
        String name = String.join(File.separator, path);

        if (name == null || name.isEmpty()) {
            return null;
        }

        return ResourceManager.class.getResourceAsStream(name.startsWith("/") ? name : ("/" + name));
    }

    protected InputStream load(String... path) {
        String name = String.join(File.separator, path);

        if (name == null || name.isEmpty()) {
            return null;
        }

        InputStream is;

        try {
            is = new FileInputStream("." + (name.startsWith("/") ? name : ("/" + name)));
        } catch (FileNotFoundException ex) {
            is = null;
        }

        if (is == null) {
            return loadFromResources(name);
        }

        return is;
    }

    public ResourceRepresenter getResourceRepresenter(String path) {
        return new ResourceRepresenter(this, path);
    }
}
