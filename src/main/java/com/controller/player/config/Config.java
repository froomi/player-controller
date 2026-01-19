package com.controller.player.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.util.Strings;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static volatile Config instance;
    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static final Properties PROPERTIES = new Properties();
    private static final String DEFAULT_CONFIG_FILE = "config.properties";
    private final String ENVIRONMENT;

    private Config() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE)) {
            PROPERTIES.load(inputStream);
            log.info("Loaded configuration from {}", DEFAULT_CONFIG_FILE);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }

        String ENVIRONMENT_PROPERTY = "environment";
        ENVIRONMENT = getSystemProperty(ENVIRONMENT_PROPERTY);
    }

    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    public String getBaseUrl() {
        return PROPERTIES.getProperty(ENVIRONMENT + ".baseUrl");
    }

    private static String getSystemProperty(String systemProperty) {
        String value = System.getProperty(systemProperty);
        if (Strings.isNullOrEmpty(value)) {
            throw new RuntimeException("VM option '-D'" + systemProperty + " should be defined");
        } else {
            return value;
        }
    }

}
