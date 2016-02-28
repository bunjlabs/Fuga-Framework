package com.bunjlabs.fugaframework.example;

import com.bunjlabs.fugaframework.FugaApp;
import com.bunjlabs.fugaframework.example.services.ExampleService;
import java.util.concurrent.TimeUnit;

public class FugaExampleApp extends FugaApp {
    
    @Override
    public void prepare() {
        getRouter().load("routes/example.rmap");        
        
        getDependencyManager().registerDependency(new ExampleDependency());
        
        getServiceManager().registerService(ExampleService.class, 5, TimeUnit.SECONDS);
        
    }
    
    public static void main(String[] args) throws Exception {
        new FugaExampleApp().start();
    }
}
