/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fuga.configuration;

import com.bunjlabs.fuga.FugaApp;
import com.bunjlabs.fuga.resources.ResourceRepresenter;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public final class Configuration {

    private final Logger log = LogManager.getLogger(Configuration.class);

    private final Properties properties = new Properties();
    private final ResourceRepresenter resourceRepresenter;

    /**
     * Create a new configuration and load default config.
     *
     * @param app Fuga application.
     */
    public Configuration(FugaApp app) {
        this.resourceRepresenter = app.getResourceManager().getResourceRepresenter("config");
        try {
            properties.load(resourceRepresenter.loadFromClasspath("globals.properties"));
        } catch (Exception ex) {
            log.error("Unable to load global config file", ex);
        }
    }

    /**
     *
     * Load configuration file from the given path.
     *
     * @param path Path to the configuration file.
     */
    public void load(String path) {
        try {
            properties.load(resourceRepresenter.load(path));
            log.info("Configuration loaded from: {}", path);
        } catch (Exception ex) {
            log.catching(ex);
        }
    }

    /**
     * Load configuration file from the given path in classpath.
     *
     * @param path Path to the configuration file in classpath.
     */
    public void loadFromClasspath(String path) {
        try {
            properties.load(resourceRepresenter.loadFromClasspath(path));
            log.info("Configuration loaded from resources: {}", path);
        } catch (Exception ex) {
            log.catching(ex);
        }
    }

    /**
     * Set configuration entry.
     *
     * @param name Entry name.
     * @param value Entry value.
     */
    public void set(String name, String value) {
        properties.setProperty(name, value);
    }

    /**
     * Get configuration entry.
     *
     * If given entry name does not exists, the default value will be returned.
     *
     * @param name Entry name.
     * @param defaultValue Default value.
     * @return Entry value or defaultValue.
     */
    public String get(String name, String defaultValue) {
        if (properties.containsKey(name)) {
            return (String) properties.getProperty(name);
        } else {
            return defaultValue;
        }
    }

    /**
     * Get configuration entry.
     *
     * If given entry name does not exists, the null value will be returned.
     *
     * @param name Entry name.
     * @return Entry value or null.
     */
    public String get(String name) {
        return (String) properties.getProperty(name);
    }

    /**
     * Get configuration entry as integer.
     *
     * If given entry name does not exists or if the value does not contain a
     * parsable integer, the default value will be returned.
     *
     * @param name Entry name.
     * @param defaultValue Default value.
     * @return Entry value or defaultValue
     */
    public int getInt(String name, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(name));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get configuration entry as integer.
     *
     * If given entry name does not exists or if the value does not contain a
     * parsable integer, the corresponding exseption will be thrown.
     *
     * Calling this method is identical to {@code Integer.parseInt(get(name))}
     *
     * @param name Entry name.
     * @return Entry value
     */
    public int getInt(String name) throws NumberFormatException {
        return Integer.parseInt(get(name));
    }

    /**
     * Get configuration entry as boolean.
     *
     * If given entry name does not exists or if the value does not contain a
     * parsable boolean, the default value will be returned.
     *
     * @param name Entry name.
     * @param defaultValue Default value.
     * @return Entry value or defaultValue
     */
    public boolean getBoolean(String name, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(get(name));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get configuration entry as boolean.
     *
     * If given entry name does not exists or if the value does not contain a
     * parsable boolean, the corresponding exseption will be thrown.
     *
     * Calling this method is identical to
     * {@code Boolean.parseBoolean(get(name))}
     *
     * @param name Entry name.
     * @return configuration entry as boolean.
     */
    public boolean getBoolean(String name) {
        return Boolean.parseBoolean(get(name));
    }

    /**
     * Get configuration entry as list.
     *
     * If given entry name does not exists or if the value does not contain a
     * parsable list, the empty list will be returned.
     *
     * @param name Entry name.
     * @return configuration entry as list.
     */
    public List<String> getList(String name) {
        String raw = get(name);

        if (raw == null || raw.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return Stream.of(raw.split(",")).map((s) -> s.trim()).collect(Collectors.toList());
    }
}
