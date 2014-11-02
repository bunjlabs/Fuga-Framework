package com.showvars.sweetie.examples.helloworld;

import com.showvars.sweetie.SweetieApp;
import java.io.InputStreamReader;

public class HelloWorldApp {

    public static void main(String[] args) throws Exception {
        SweetieApp ss = SweetieApp.prepare();
        ss.getRouter().load(new InputStreamReader(HelloWorldApp.class.getResourceAsStream("/helloworld.routesmap")));
        ss.getStarted();
    }

}
