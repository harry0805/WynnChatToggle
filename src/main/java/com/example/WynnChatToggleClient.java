package com.example;

import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;

import com.example.command.chatCommand;
import com.example.command.aCommand;
import com.example.command.wctCommand;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

public class WynnChatToggleClient implements ClientModInitializer {
    public static final String MOD_ID = "wynn_chat_toggle";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ChatChannel.Channel currentChannel = ChatChannel.All;
    public static ChatChannel.Channel channelOverride = null;
    public static ChatChannel.Channel lastOverrideChannel;
    public static long lastOverrideTime;
    public static int chainOverrideInterval = 100;

    public static File configLocation = new File(FabricLoader.getInstance().getConfigDir().toFile(), MOD_ID);
    public static JsonObject overrideConfig;
    private static InputOverrides inputOverrides;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Starting WynnChatToggle Client Initialization...");
        loadConfigs();
        registerCommands();
        registerSendMessageListener();

        inputOverrides = new InputOverrides();
        inputOverrides.registerChatMessageListener();

        LOGGER.info("WynnChatToggle Client Initialized");
    }

    public void reload() {
        LOGGER.info("Starting WynnChatToggle Reload...");
        loadConfigs();

        inputOverrides.loadHashMap();
        LOGGER.info("WynnChatToggle Reloaded!");
    }

    private static void loadConfigs() {
        LOGGER.info("path to config {}", configLocation.getAbsolutePath());
        // Create the config directory if it doesn't exist
        if (!WynnChatToggleClient.configLocation.exists()) {
            boolean created = WynnChatToggleClient.configLocation.mkdirs();
             assert created;
        }

        ConfigDownload configDownload = new ConfigDownload();

        // Download the config file
        File configFile = configDownload.download();
        if (configFile == null) {
            LOGGER.warn("Failed to download overrideConfig.json");
        }
        overrideConfig = LoadConfig.loadConfig(configFile);
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            //  new exampleCommand().register(dispatcher);
            new chatCommand().register(dispatcher);
            new aCommand().register(dispatcher);
            wctCommand wct = new wctCommand(this);
            wct.register(dispatcher);
        });
    }

    public static void setChatChannel(ChatChannel.Channel channel) {
        currentChannel = channel;
    }

    private boolean sendMessageInChannel(String message) {
        ChatChannel.Channel messageChannel;
        // Check if ChannelOverride is on
        if (channelOverride != null) {
            messageChannel = channelOverride;
            channelOverride = null;

            lastOverrideChannel = messageChannel;
            lastOverrideTime = System.currentTimeMillis();
        } else if (lastOverrideTime + chainOverrideInterval > System.currentTimeMillis()) {
            messageChannel = lastOverrideChannel;
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
    }

    private void registerSendMessageListener() {
        ClientSendMessageEvents.ALLOW_CHAT.register(this::sendMessageInChannel);
    }

    private static void sendCommandMessage(String message, String command) {
        String commandMessage = command + " " + message;
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand(commandMessage);
    }

    public static void printChat(String message, Formatting format) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null) {  // Check if the player exists (i.e., the client is in a game)
            client.player.sendMessage(Text.literal(message).formatted(format), false);
        }
    }
}