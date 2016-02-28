package com.bunjlabs.fugaframework.example.services;

import com.bunjlabs.fugaframework.services.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleService extends Service {
    private static final Logger log = LogManager.getLogger(ExampleService.class);
    
    @Override
    public void onCreate() {
        log.info("I'm was created!");
    }
    
    @Override
    public void onUpdate() {
        log.info("I'm was updated!");
    }

}
