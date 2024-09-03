package com.example.command;

import com.example.WynnChatToggleClient;
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

public class wctCommand {
    WynnChatToggleClient thisMod;

    public wctCommand(WynnChatToggleClient thisMod) {
        this.thisMod = thisMod;
    }

    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("wct")
                .then(argument("action", StringArgumentType.string())
                        .suggests(this::suggestCommands)
                        .executes(this::commandExecute)
                )
        );
    }

    private int commandExecute(CommandContext<FabricClientCommandSource> context) {
        String action = context.getArgument("action", String.class).toLowerCase();
        if (action.equals("reload")) {
            WynnChatToggleClient.printChat("WynnChatToggle is reloading...", Formatting.YELLOW);
            thisMod.reload();
            WynnChatToggleClient.printChat("Reloaded Successfully!", Formatting.GREEN);
            return 1;
        }

        WynnChatToggleClient.printChat("Unknown action!", Formatting.RED);
        return 0;
    }
    private CompletableFuture<Suggestions> suggestCommands(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        builder.suggest("reload");
        return builder.buildFuture();
    }
}
