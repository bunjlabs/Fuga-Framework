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
package com.bunjlabs.fuga.examples.i18n;

import com.bunjlabs.fuga.foundation.Controller;
import com.bunjlabs.fuga.foundation.Result;
import com.bunjlabs.fuga.views.ViewException;

public class I18nExampleController extends Controller {

    public Result language() {
        String langCode = ctx.lang();

        return redirect(ctx.urls().that(langCode));
    }

    public Result language(String langCode) {
        ctx.lang(langCode);
        return proceed();
    }

    public Result index() throws ViewException {
        return ok(view("i18n.html"));
    }
}
