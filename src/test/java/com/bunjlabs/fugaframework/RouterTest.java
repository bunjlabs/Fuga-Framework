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
package com.bunjlabs.fugaframework;

import com.bunjlabs.fugaframework.router.Router;
import org.junit.Test;

public class RouterTest {

    private FugaApp prepareTestApp() {
        FugaApp fa = new FugaApp() {

            @Override
            public void prepare() {
            }
        };
        return fa;
    }

    @Test
    public void compileTest() throws Exception {
        Router r = prepareTestApp().getRouter();

        r.loadFromString("");
        r.loadFromString("GET ok(\"ok\")");
        r.loadFromString("GET POST ok(\"ok\")");
        r.loadFromString("GET GET ok(\"ok\")");
        r.loadFromString("$/ ok(\"ok\")");
        r.loadFromString("ok(\"ok\")");
        r.loadFromString("{}");
        r.loadFromString("{ok(\"ok\")}");
        r.loadFromString("{{{{{{{{{ok(\"ok\")}}}}}}}}}");
        r.loadFromString("GET{POST{OPTIONS{ok(\"ok\")}}}");
        r.loadFromString("use com.example");

        r.loadFromString("GET ok(\"ok\") POST ok(\"ok\") OPTIONS ok(\"ok\")");

    }
}
