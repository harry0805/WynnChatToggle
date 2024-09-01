package com.example.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;


import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class exampleCommand {
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("testcmd")
                .then(argument("args", StringArgumentType.greedyString())
                        .suggests(this::suggestCommands)
                        .executes(this::commandExecute)
                )
        );
    }
    private int commandExecute(CommandContext<FabricClientCommandSource> context) {
        String args = context.getArgument("args", String.class);
        String command = "tp " + args;

        // Send the command to the chat as if the player typed it
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand(command);
        return 1;
    }
    private CompletableFuture<Suggestions> suggestCommands(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        builder.suggest("my_custom_argument1");
        builder.suggest("my_custom_argument2");
        // Add more custom suggestions here
        return builder.buildFuture();
    }
}
