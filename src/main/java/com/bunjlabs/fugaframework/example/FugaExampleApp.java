package com.bunjlabs.fugaframework.example;

import com.bunjlabs.fugaframework.FugaApp;


public class FugaExampleApp extends FugaApp {

    @Override
    public void prepare() {
        this.getRouter().loadFromResources("example/routes.map");
        
    }

    public static void main(String[] args) throws Exception {
        new FugaExampleApp().start();
    }
}
