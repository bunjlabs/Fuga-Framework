package com.bunjlabs.fugaframework.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServiceManager {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void registerService(Service service, long delay, TimeUnit timeUnit) {
        scheduler.scheduleAtFixedRate(service, 0, delay, timeUnit);
        
    }

    public void registerService(Service service, long initialDelay, long period, TimeUnit timeUnit) {
        scheduler.scheduleAtFixedRate(service, initialDelay, period, timeUnit);
    }
}
