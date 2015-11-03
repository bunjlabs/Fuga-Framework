package com.showvars.fugaframework.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Configuration {

    private static final Logger log = LogManager.getLogger(Configuration.class);

    private final Properties p = new Properties();

    public void load(InputStream input) {
        try {
            p.load(input);
            log.info("Configuration loaded from stream");
        } catch (Exception ex) {
            log.catching(ex);
        }
    }

    public void load(File file) {
        try {
            p.load(new FileInputStream(file));
            log.info("Configuration loaded from: {}", file.getPath());
        } catch (Exception ex) {
            log.catching(ex);
        }
    }

    public void load(String path) {
        try {
            p.load(new FileInputStream(path));
            log.info("Configuration loaded from: {}", path);
        } catch (Exception ex) {
            log.catching(ex);
        }
    }

    public void loadFromResoures(String path) {
        try {
            p.load(Configuration.class.getResourceAsStream("/" + path));
            log.info("Configuration loaded from resources: {}", "/" + path);
        } catch (Exception ex) {
            log.catching(ex);
        }
    }

    public void set(String name, String value) {
        p.setProperty(name, value);
    }

    public String get(String name, String defaultValue) {
        if (p.containsKey(name)) {
            return (String) p.getProperty(name);
        } else {
            return defaultValue;
        }
    }

    public int getInt(String name, int defaultValue) {
        if (p.containsKey(name)) {
            return Integer.parseInt(p.getProperty(name));
        } else {
            return defaultValue;
        }
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        if (p.containsKey(name)) {
            return Boolean.parseBoolean(p.getProperty(name));
        } else {
            return defaultValue;
        }
    }
}
