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
package com.bunjlabs.fuga.templates;

import javax.script.CompiledScript;

/**
 *
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class ScriptBlock {

    private CompiledScript script;

    /**
     *
     * @param script compiled script of this block
     */
    public ScriptBlock(CompiledScript script) {
        if (script == null) {
            throw new NullPointerException("CompiledScript is null");
        }

        this.script = script;
    }

    /**
     *
     * @return compiled script of this block
     */
    public CompiledScript getScript() {
        return script;
    }

}
