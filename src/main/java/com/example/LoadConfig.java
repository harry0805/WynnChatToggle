package com.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.File;

public class LoadConfig {
    public static JsonObject loadConfig(File configFile) {
        try (FileReader reader = new FileReader(configFile)) {
            // Parse the JSON file into a JsonObject
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();
            WynnChatToggleClient.LOGGER.info("Configuration loaded successfully");
            return config;
        } catch (Exception e) {
            WynnChatToggleClient.LOGGER.info("Failed to load configuration: ", e);
            return null;
        }
    }
}
