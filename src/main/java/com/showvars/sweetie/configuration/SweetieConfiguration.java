package com.showvars.sweetie.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SweetieConfiguration {

    private final Properties p = new Properties();

    public void load(InputStream input) throws IOException {
        p.load(input);
    }

    public void load(File file) throws IOException {
        load(new FileInputStream(file));
    }

    public void load(String path) throws IOException {
        load(new FileInputStream(path));
    }

    public void loadFromResoures(String path) throws IOException {
        load(SweetieConfiguration.class.getResourceAsStream("/" + path));
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
