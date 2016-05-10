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
package com.bunjlabs.fugaframework.example.services;

import com.bunjlabs.fugaframework.dependency.Inject;
import com.bunjlabs.fugaframework.example.ExampleDependency;
import com.bunjlabs.fugaframework.handlers.RequestHandler;
import com.bunjlabs.fugaframework.services.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleService extends Service {

    private static final Logger log = LogManager.getLogger(ExampleService.class);

    private final RequestHandler re;
    private final ExampleDependency ed;

    @Inject
    public ExampleService(RequestHandler re, ExampleDependency ed) {
        this.re = re;
        this.ed = ed;

    }

    @Override
    public void onCreate() {
        log.info("I'm was created!");
        log.info("Request handler implementation is: {}", re.getClass().getName());
    }

    @Override
    public void onUpdate() {
        log.info("I'm was updated!");
    }

}
