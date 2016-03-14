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
package com.bunjlabs.fugaframework.services;

import com.bunjlabs.fugaframework.FugaApp;
import com.bunjlabs.fugaframework.dependency.InjectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServiceManager {

    private static final Logger log = LogManager.getLogger(ServiceManager.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<Class<? extends Service>, Service> services = new HashMap<>();
    private final FugaApp app;

    public ServiceManager(FugaApp app) {
        this.app = app;
    }

    public void registerService(Class<? extends Service> service) {
        try {
            Service serviceInstance = service.newInstance();

            app.getDependencyManager().injectDependencies(serviceInstance);

            services.put(service, serviceInstance);
            app.getDependencyManager().registerDependency(service);
            
            serviceInstance.onCreate();
        } catch (InstantiationException | IllegalAccessException ex) {
            log.catching(ex);
        } catch (InjectException ex) {
            log.error("Unable to inject dependencies to the service", ex);
        }
    }

    public void registerService(Class<? extends Service> service, long updateTime, TimeUnit timeUnit) {
        registerService(service);

        Service serviceInstance = services.get(service);

        if (serviceInstance == null) {
            log.error("Unable to shedule service update");
            return;
        }

        scheduler.scheduleAtFixedRate(serviceInstance::onUpdate, 0, updateTime, timeUnit);
    }
}
