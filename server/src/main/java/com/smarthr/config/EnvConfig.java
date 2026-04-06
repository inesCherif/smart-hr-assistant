package com.smarthr.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Handles environment configurations to ensure no keys or secrets are hardcoded.
 * Reads from .env file or system environment variables.
 **/
public class EnvConfig {
    private static final Dotenv dotenv = loadDotenv();

    private static Dotenv loadDotenv() {
        try {
            return Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
        } catch (Exception e) {
            System.err.println("Warning: .env configuration error or file missing.");
            return null;
        }
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static String get(String key, String defaultValue) {
        String val = null;
        
        // 1. Try to get from .env file
        if (dotenv != null) {
            val = dotenv.get(key);
        }
        
        // 2. Try to get from System Variables if .env doesn't have it (good for Docker)
        if (val == null || val.trim().isEmpty()) {
            val = System.getenv(key);
        }
        
        return (val != null && !val.trim().isEmpty()) ? val : defaultValue;
    }
}
