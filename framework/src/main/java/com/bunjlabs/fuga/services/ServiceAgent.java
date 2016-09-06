package com.bunjlabs.fuga.services;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

class ServiceAgent<T extends Service> {

    private final T service;
    private ScheduledFuture scheduledFuture;

    ServiceAgent(T service) {
        this.service = service;
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
