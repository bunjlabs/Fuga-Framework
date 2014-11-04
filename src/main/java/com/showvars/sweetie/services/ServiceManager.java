package com.showvars.sweetie.services;

import com.showvars.sweetie.SweetieApp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServiceManager {

    private final SweetieApp app;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ServiceManager(SweetieApp app) {
        this.app = app;
    }

    public void registerService(Service service, long delay, TimeUnit timeUnit) {
        scheduler.schedule(service, delay, timeUnit);
    }

    public void registerService(Service service, long initialDelay, long period, TimeUnit timeUnit) {
        scheduler.scheduleAtFixedRate(service, initialDelay, period, timeUnit);
    }
}
