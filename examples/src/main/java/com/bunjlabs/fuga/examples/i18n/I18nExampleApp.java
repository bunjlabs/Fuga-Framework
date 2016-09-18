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

import com.bunjlabs.fuga.FugaApp;
import static com.bunjlabs.fuga.FugaApp.launch;
import com.bunjlabs.fuga.dependency.InjectException;
import com.bunjlabs.fuga.templates.TemplateViewRenderer;

public class I18nExampleApp extends FugaApp {

    @Override
    public void prepare() throws InjectException {
        getRouter().load("i18n.froutes");
        getConfiguration().load("i18nexample.properties");

        setViewRenderer(TemplateViewRenderer.class);
    }

    public static void main(String[] args) throws Exception {
        launch(I18nExampleApp.class);
    }
}
