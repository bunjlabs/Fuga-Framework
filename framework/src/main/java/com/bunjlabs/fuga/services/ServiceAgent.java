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
package com.bunjlabs.fuga.services;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ServiceAgent<T extends Service> {

    private final Logger log = LogManager.getLogger(this.getClass());

    private final T service;
    private ScheduledFuture scheduledFuture;

    ServiceAgent(T service) {
        this.service = service;
    }

    public void fireUpdate() {
        try {
            this.service.onUpdate();
        } catch (Exception e) {
            log.fatal("Catching exception from service {}: {}", this.service.getClass().getName(), e);
        }
    }

    public T getService() {
        return service;
    }

    public Future getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public boolean isSheduled() {
        return scheduledFuture != null;
    }

}
