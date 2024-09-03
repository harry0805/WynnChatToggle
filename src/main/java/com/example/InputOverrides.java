package com.example;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;

import com.google.gson.JsonElement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class InputOverrides {

    private Map<Pattern, String> regexActions;

    private static final Pattern FORMATTING_CODE_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    
    public InputOverrides() {
        loadHashMap();
    }

    public void loadHashMap() {
        regexActions = new HashMap<>();
        // Convert the JsonObject into a HashMap<Pattern, String>
        for (Map.Entry<String, JsonElement> entry : WynnChatToggleClient.overrideConfig.entrySet()) {
            // Compile the regex pattern from the JSON key
            Pattern pattern = Pattern.compile(entry.getKey());

            // Get the corresponding value, if it's not null
            String value = entry.getValue().isJsonNull() ? null : entry.getValue().getAsString();

            // Put the pattern and value into the map
            regexActions.put(pattern, value);
        }
    }

    public void registerChatMessageListener() {
        ClientReceiveMessageEvents.GAME.register(this::matchMessage);
    }

    private void matchMessage(Text message, Boolean overlay) {
        String textMessage = message.getString();

        // Clean up the message by removing Minecraft formatting codes
        textMessage = cleanMessage(textMessage);

        // Check each regex pattern to see if it matches the incoming message
        for (Map.Entry<Pattern, String> entry : regexActions.entrySet()) {
            Matcher matcher = entry.getKey().matcher(textMessage);
            if (matcher.find()) {
                // If the message matches, set the override channel to its value
                WynnChatToggleClient.LOGGER.info("Matched message: {}", message.getString());
                ChatChannel.Channel channel = null;
                if (entry.getValue() != null) {
                    channel = ChatChannel.getChannelById(entry.getValue());
                }
                OverrideChannel(channel);
                break;  // Stop checking other patterns if one is matched
            }
        }
    }

    private String cleanMessage(String message) {
        // Remove all Minecraft formatting codes from the message
        return FORMATTING_CODE_PATTERN.matcher(message).replaceAll("");
    }

    private void OverrideChannel(ChatChannel.Channel channel) {
        WynnChatToggleClient.channelOverride = channel;
    }
}
