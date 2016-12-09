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
package com.bunjlabs.fuga.views;

import com.bunjlabs.fuga.foundation.Context;
import java.io.PrintStream;

public interface ViewRenderer {

    /**
     * Render view to the print stream.
     *
     * @param name View name.
     * @param ctx Request context.
     * @param output Output print stream.
     * @throws ViewException if any error is occurred while rendering
     */
    public void render(String name, Context ctx, PrintStream output) throws ViewException;

    /**
     * Render view to the string.
     *
     * @param name View name.
     * @param ctx Request context.
     * @return string with rendered view.
     * @throws ViewException if any error is occurred while rendering
     */
    public String renderToString(String name, Context ctx) throws ViewException;

}
