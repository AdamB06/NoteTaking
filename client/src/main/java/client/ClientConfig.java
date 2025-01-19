package client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ClientConfig {
    private static final String CONFIG_FILE = "configuration/config.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String preferredLanguage;
    private String serverUrl;

    /**
     * Get the preferred language of the client
     * @return The preferred language of the client
     */
    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    /**
     * Set the preferred language of the client
     * @param preferredLanguage The preferred language of the client
     */
    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
        saveConfig();
    }

    /**
     * Get the server URL
     * @return The server URL
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * Set the server URL
     * @param serverUrl The server URL
     */
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        saveConfig();
    }

    /**
     * Load the configuration file
     * @return The configuration file contents
     */
    public static ClientConfig loadConfig() {
        try (InputStream inputStream = ClientConfig.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                return objectMapper.readValue(inputStream, ClientConfig.class);
            } else {
                throw new IOException("Configuration file not found");
            }
        } catch (IOException e) {
            ClientConfig defaultConfig = new ClientConfig();
            defaultConfig.setPreferredLanguage("en");
            defaultConfig.setServerUrl("http://localhost:8080/");
            defaultConfig.saveConfig();
            return defaultConfig;
        }
    }

    /**
     * Save/Update the configuration file
     */
    private void saveConfig() {
        try {
            objectMapper.writeValue(new File(getClass().getClassLoader()
                    .getResource(CONFIG_FILE).getFile()), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}