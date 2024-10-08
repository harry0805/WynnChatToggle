package com.example.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.minecraft.util.Formatting;

import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

import com.example.WynnChatToggleClient;

import com.example.ChatChannel;

public class chatCommand {
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("chat")
                .then(argument("ChatType", StringArgumentType.string())
                        .suggests(this::suggestCommands)
                        .executes(this::commandExecute)
                )
        );
    }

    private int commandExecute(CommandContext<FabricClientCommandSource> context) {
        String chatType = context.getArgument("ChatType", String.class).toLowerCase();
        ChatChannel.Channel resolvedChatType = switch (chatType) {
            case "a", "all" -> ChatChannel.All;
            case "p", "party" -> ChatChannel.Party;
            case "g", "guild" -> ChatChannel.Guild;
            default -> null;
        };

        if (resolvedChatType == null) {
            WynnChatToggleClient.printChat("Unknown channel type!", Formatting.RED);
            return 0;
        }

        WynnChatToggleClient.setChatChannel(resolvedChatType);

        WynnChatToggleClient.printChat(String.format("You are now in the %s channel", resolvedChatType.getFormattedName()), Formatting.GREEN);
        return 1;
    }
    private CompletableFuture<Suggestions> suggestCommands(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        builder.suggest("all");
        builder.suggest("party");
        builder.suggest("guild");
        return builder.buildFuture();
    }
}
