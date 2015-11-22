package com.bunjlabs.fugaframework.example;

import com.bunjlabs.fugaframework.FugaApp;


public class FugaExampleApp extends FugaApp {

    @Override
    public void prepare() {
        getRouter().load("routes/example.rmap");        
        
    }

    public static void main(String[] args) throws Exception {
        new FugaExampleApp().start();
    }
}
