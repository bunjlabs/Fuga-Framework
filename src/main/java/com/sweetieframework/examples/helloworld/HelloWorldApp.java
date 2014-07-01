package com.sweetieframework.examples.helloworld;

import com.sweetieframework.SweetieApp;
import com.sweetieframework.examples.helloworld.controllers.HelloWorldIndexController;

public class HelloWorldApp {
    
    public static void main(String[] args) throws Exception {
        SweetieApp ss = SweetieApp.prepare();
        ss.getRouter().register(new HelloWorldIndexController());
        ss.getStarted();
    }
    
}
