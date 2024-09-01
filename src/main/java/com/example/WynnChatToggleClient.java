package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;

import com.example.command.chatCommand;
import com.example.command.aCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class WynnChatToggleClient implements ClientModInitializer {
    public static final String MOD_ID = "wynn_chat_toggle";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ChatChannel.Channel currentChannel = ChatChannel.All;
    public static ChatChannel.Channel channelOverride = null;

    @Override
    public void onInitializeClient() {
        registerCommands();
        registerChatEventListener();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
//            new exampleCommand().register(dispatcher);
            new chatCommand().register(dispatcher);
            new aCommand().register(dispatcher);
        });
    }

    public static void setChatChannel(ChatChannel.Channel channel) {
        currentChannel = channel;
    }

    private void registerChatEventListener() {
        ClientSendMessageEvents.ALLOW_CHAT.register((message) -> {
            ChatChannel.Channel messageChannel;
            // Check if ChannelOverride is on
            if (channelOverride != null) {
                messageChannel = channelOverride;
                channelOverride = null;
            } else {
                messageChannel = currentChannel;
            }

            if (!Objects.equals(messageChannel.id, "all")) {
                sendCommandMessage(message, messageChannel.command);
                // Return false to cancel the chat message from being sent
                return false;
            }
            // Otherwise, allow the message to be sent
            return true;
        });
    }

    private static void sendCommandMessage(String message, String command) {
        String commandMessage = command + " " + message;
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand(commandMessage);
    }
}