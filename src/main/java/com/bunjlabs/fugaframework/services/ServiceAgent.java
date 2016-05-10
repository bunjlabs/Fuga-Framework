package com.bunjlabs.fugaframework.services;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

class ServiceAgent<T extends Service> {

    private final Service service;
    private ScheduledFuture scheduledFuture;

    ServiceAgent(Service service) {
        this.service = service;
    }

    public Service getService() {
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
