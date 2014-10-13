package com.sweetieframework.examples.helloworld;

import com.sweetieframework.SweetieApp;
import java.io.InputStreamReader;

public class HelloWorldApp {

    public static void main(String[] args) throws Exception {
        SweetieApp ss = SweetieApp.prepare();
        ss.getRouter().load(new InputStreamReader(HelloWorldApp.class.getResourceAsStream("/helloworld.routesmap")));
        ss.getStarted();
    }

}
