package com.bunjlabs.fugaframework.configuration;

import com.bunjlabs.fugaframework.FugaApp;
import com.bunjlabs.fugaframework.resources.ResourceManager;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public final class Configuration {
    
    private static final Logger log = LogManager.getLogger(Configuration.class);
    
    private final Properties properties = new Properties();
    private final ResourceManager resourceManager;
    
    public Configuration(FugaApp app) {
        this.resourceManager = app.getResourceManager();
        
        try {
            properties.load(resourceManager.loadFromResources("globals.properties"));
        } catch (Exception ex) {
            log.error("Unable to load global config file", ex);
        }
    }
    
    public void load(String path) {
        try {
            properties.load(resourceManager.load(path));
            log.info("Configuration loaded from: {}", path);
        } catch (Exception ex) {
            log.catching(ex);
        }
    }
    
    public void loadFromResources(String path) {
        try {
            properties.load(resourceManager.loadFromResources(path));
            log.info("Configuration loaded from resources: {}", path);
        } catch (Exception ex) {
            log.catching(ex);
        }
    }
    
    public void set(String name, String value) {
        properties.setProperty(name, value);
    }
    
    public String get(String name, String defaultValue) {
        if (properties.containsKey(name)) {
            return (String) properties.getProperty(name);
        } else {
            return defaultValue;
        }
    }
    
    public int getInt(String name, int defaultValue) {
        if (properties.containsKey(name)) {
            return Integer.parseInt(properties.getProperty(name));
        } else {
            return defaultValue;
        }
    }
    
    public boolean getBoolean(String name, boolean defaultValue) {
        if (properties.containsKey(name)) {
            return Boolean.parseBoolean(properties.getProperty(name));
        } else {
            return defaultValue;
        }
    }
}
