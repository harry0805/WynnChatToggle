package com.example.command;

import com.example.ChatChannel;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import java.util.Objects;

import static com.example.WynnChatToggleClient.channelOverride;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class aCommand {
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("a")
                .then(argument("msg", StringArgumentType.greedyString())
                        .executes(this::commandExecute)
                )
        );
    }
    private int commandExecute(CommandContext<FabricClientCommandSource> context) {
        String message = context.getArgument("msg", String.class);
        channelOverride = ChatChannel.All;
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatMessage(message);
        return 1;
    }
}
