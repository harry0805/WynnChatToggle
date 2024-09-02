package com.example;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class InputOverrides {

    private final Map<Pattern, ChatChannel.Channel> regexActions;

    private static final Pattern FORMATTING_CODE_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");

    public InputOverrides() {
        this.regexActions = new HashMap<>();

        // An Example Match
        regexActions.put(Pattern.compile("!Example .*"), ChatChannel.All);
        // Trade market chat input
        regexActions.put(Pattern.compile("(?s).+ Type the item name or type 'cancel' to.+cancel:.+"), ChatChannel.All);
        // Party finder Message input
        regexActions.put(Pattern.compile("Party Finder: Type in chat the description you want to use for your party \\(max 140 characters or cancel\\):"), ChatChannel.All);
        // Cancel chat input
        regexActions.put(Pattern.compile(".+ You moved and your chat input was canceled\\."), null);

    }

    public void registerChatMessageListener() {
        ClientReceiveMessageEvents.GAME.register(this::matchMessage);
    }

    private void matchMessage(Text message, Boolean overlay) {
        String textMessage = message.getString();

        // Clean up the message by removing Minecraft formatting codes
        textMessage = cleanMessage(textMessage);

        // Check each regex pattern to see if it matches the incoming message
        for (Map.Entry<Pattern, ChatChannel.Channel> entry : regexActions.entrySet()) {
            Matcher matcher = entry.getKey().matcher(textMessage);
            if (matcher.find()) {
                // If the message matches, run the associated action
                ChatChannel.Channel channel = entry.getValue();
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
