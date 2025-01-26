package agh.ics.oop.model.util;

import java.io.*;
import java.util.Properties;



public class Config {
    private static Config instance;
    private final Properties properties;
    private String configFile;

    private Config() {
        properties = new Properties();
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void initialize(String fileName) {
        if (this.configFile != null) {
            throw new IllegalStateException("Singleton is already initialized");
        }
        this.configFile = String.format("%s%s%s%s%s%s%s%s%s", "src", File.separator, "main",File.separator, "resources", File.separator, "configs", File.separator, fileName);
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
        try (InputStream input = new FileInputStream(configFile)) {
            properties.load(input);
        }
    }

    public void save() throws IOException {
        try (OutputStream output = new FileOutputStream(configFile)) {
            properties.store(output, "Simulation Configuration");
        }
    }

    public static void resetForTests() {
        instance = null;
    }
}