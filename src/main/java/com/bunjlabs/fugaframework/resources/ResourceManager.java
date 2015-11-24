package com.bunjlabs.fugaframework.resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class ResourceManager {

    public InputStream loadFromResources(String name) {
        if (name == null) {
            return null;
        }

        return ResourceManager.class.getResourceAsStream(name.startsWith("/") ? name : ("/" + name));
    }

    public InputStream load(String name) {
        InputStream is;

        if (name == null) {
            return null;
        }

        try {
            is = new FileInputStream("." + (name.startsWith("/") ? name : ("/" + name)));
        } catch (FileNotFoundException ex) {
            is = null;
        }

        if (is == null) {
            return loadFromResources(name);
        }

        return is;
    }
}
