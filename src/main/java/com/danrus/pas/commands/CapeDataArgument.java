package com.danrus.pas.commands;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CapeDataArgument implements ArgumentType<CapeData> {

    private static final List<String> EXAMPLES = List.of("Danrus110_", "4da4ce379e8b0d00");
    private static final SimpleCommandExceptionType SKIN_NOT_FOUND = new SimpleCommandExceptionType(Component.translatable("commands.pas.skin_not_found"));

    @Override
    public CapeData parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readUnquotedString();
        CapeData data = PasManager.getInstance().getCapeDataManager().findData(NameInfo.parse(name));
        if (data == null) {
            throw SKIN_NOT_FOUND.create();
        }
        return data;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        HashMap<String, CapeData> allData = PasManager.getInstance().getCapeDataManager().getGameData();
        allData.forEach((name, capeData) -> {
            if (name.toLowerCase().startsWith(builder.getRemaining().toLowerCase()) && !name.contains("|")) {
                builder.suggest(name);
            }
        });
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static CapeData getData(CommandContext<?> context, String name) {
        return context.getArgument(name, CapeData.class);
    }
}
