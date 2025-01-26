package agh.ics.oop.model.util;

import java.io.*;
import java.util.Properties;

public class Config {
    private static Config instance;
    private final Properties properties;
    private static final String CONFIG_FILE = "src/main/resources/config.properties";

    private Config() {
        properties = new Properties();
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public int getInt(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing configuration key: " + key);
        }
        return Integer.parseInt(value);
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public void set(String key, Object value) {
        properties.setProperty(key, value.toString());
    }

    public void load() throws IOException {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        }
    }

    public void save() throws IOException {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "Simulation Configuration");
        }
    }
}