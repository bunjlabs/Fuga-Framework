package com.bunjlabs.fugaframework.configuration;

import java.io.IOException;
import java.util.Properties;

public class GlobalConfiguration {

    private final Properties properties = new Properties();

    public GlobalConfiguration() {
        try {
            properties.load(GlobalConfiguration.class.getResourceAsStream("/globals.properties"));
        } catch (IOException ex) {
        }
    }

    public String get(String name) {
        return properties.getProperty(name, "");
    }
}
