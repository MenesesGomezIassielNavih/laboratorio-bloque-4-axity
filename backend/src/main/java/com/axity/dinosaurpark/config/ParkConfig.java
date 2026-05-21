package com.axity.dinosaurpark.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton thread-safe que centraliza la configuracion del parque.
 *
 * <p>Implementado con double-checked locking + volatile siguiendo
 * Effective Java 3a ed., Item 83 (Bloch, Addison-Wesley 2017).
 */
public final class ParkConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ParkConfig.class);
    private static final String DEFAULT_FILE = "park.properties";

    private static volatile ParkConfig instance;
    private final Properties props;

    private ParkConfig() {
        this.props = new Properties();
        loadProperties();
    }

    public static ParkConfig getInstance() {
        ParkConfig local = instance;
        if (local == null) {
            synchronized (ParkConfig.class) {
                local = instance;
                if (local == null) {
                    instance = local = new ParkConfig();
                }
            }
        }
        return local;
    }

    private void loadProperties() {
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(DEFAULT_FILE)) {
            if (is == null) {
                LOG.warn("park.properties no encontrado en classpath; usando defaults");
                return;
            }
            props.load(is);
            LOG.info("Configuracion cargada: {} entradas", props.size());
        } catch (IOException ex) {
            LOG.error("No se pudo leer {} - causa: {}", DEFAULT_FILE, ex.getMessage());
        }
    }

    public int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException nfe) {
            LOG.warn("Valor invalido para {}: '{}' - usando default {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public double getDouble(String key, double defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException nfe) {
            LOG.warn("Valor invalido para {}: '{}' - usando default {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}