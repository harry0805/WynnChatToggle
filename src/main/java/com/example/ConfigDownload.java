package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ConfigDownload {

    private static final String CONFIG_URL = "https://raw.githubusercontent.com/harry0805/WynnChatToggle/main/overrideConfig.json"; // Replace with your actual URL
    private static final String CONFIG_FILE_NAME = "overrideConfig.json";

    public File download() {
        WynnChatToggleClient.LOGGER.info("Starting download " + CONFIG_FILE_NAME + " from " + CONFIG_URL );
        try {
            // Create the config file path
            File configFile = new File(WynnChatToggleClient.configLocation, CONFIG_FILE_NAME);

            // Create a URI from the string, then convert to a URL
            URI uri = new URI(CONFIG_URL);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(configFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                WynnChatToggleClient.LOGGER.info("Successfully downloaded configuration file " + CONFIG_FILE_NAME);

                return configFile;
            }
        } catch (Exception e) {
            WynnChatToggleClient.LOGGER.info("Error Downloading File " + CONFIG_FILE_NAME + ": ", e);
            return null;
        }
    }
}
