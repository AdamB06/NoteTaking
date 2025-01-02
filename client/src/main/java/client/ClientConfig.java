package client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ClientConfig {
    private static final String CONFIG_FILE = "client/src/main/resources/config.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String preferredLanguage;
    private String serverUrl;

    // Getters and setters
    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
        saveConfig();
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        saveConfig();
    }

    // Load configuration from file
    public static ClientConfig loadConfig() {
        try {
            return objectMapper.readValue(new File(CONFIG_FILE), ClientConfig.class);
        } catch (IOException e) {
            ClientConfig defaultConfig = new ClientConfig();
            defaultConfig.setPreferredLanguage("en");
            defaultConfig.setServerUrl("http://localhost:8080");
            defaultConfig.saveConfig();
            return defaultConfig;
        }
    }

    // Save configuration to file
    private void saveConfig() {
        try {
            objectMapper.writeValue(new File(CONFIG_FILE), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}