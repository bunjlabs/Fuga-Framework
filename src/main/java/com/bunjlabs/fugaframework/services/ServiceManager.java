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
    private final Map<Class<? extends Service>, ServiceAgent> services = new HashMap<>();
    private final FugaApp app;

    public ServiceManager(FugaApp app) {
        this.app = app;
    }

    public void register(Class<? extends Service> service) {
        try {
            Service serviceInstance = app.getDependencyManager().registerAndInject(service);

            services.put(service, new ServiceAgent(serviceInstance));

            serviceInstance.onCreate();
        } catch (InjectException ex) {
            log.error("Unable to inject dependencies to the service", ex);
        }
    }

    public void register(Class<? extends Service> service, long updateTime, TimeUnit timeUnit) {
        register(service);
        schedule(service, updateTime, timeUnit);
    }

    public void schedule(Class<? extends Service> service, long updateTime, TimeUnit timeUnit) {
        ServiceAgent serviceAgent = services.get(service);

        if (serviceAgent == null) {
            log.error("Unable to shedule service update. Service " + service.getName() + " not found.");
            return;
        }

        if (serviceAgent.isSheduled()) {
            log.error("Unable to shedule service update. Service " + service.getName() + " already scheduled.");
            return;
        }

        serviceAgent.setScheduledFuture(scheduler.scheduleAtFixedRate(serviceAgent.getService()::onUpdate, 0, updateTime, timeUnit));
    }

    public void unregister(Class<? extends Service> service) {
        ServiceAgent serviceAgent = services.get(service);

        if (serviceAgent.isSheduled()) {
            serviceAgent.getScheduledFuture().cancel(true);
        }

        services.remove(service);
    }

}
