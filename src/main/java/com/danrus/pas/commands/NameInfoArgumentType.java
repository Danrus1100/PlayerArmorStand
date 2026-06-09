package com.danrus.pas.commands;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

public abstract class NameInfoArgumentType<T extends DataHolder> implements ArgumentType<NameInfo> {

    public static NameInfo getNameInfo(CommandContext<?> ctx, String name) {
        return ctx.getArgument(name, NameInfo.class);
    }

    public static NameInfoArgumentType<SkinData> forSkin() {
        return new NameInfoArgumentType<>() {
            @Override
            DataRepository<SkinData> getDataRepository() {
                return PasManager.getInstance().getSkinDataManager();
            }
        };
    }

    public static NameInfoArgumentType<CapeData> forCape() {
        return new NameInfoArgumentType<>() {
            @Override
            DataRepository<CapeData> getDataRepository() {
                return PasManager.getInstance().getCapeDataManager();
            }
        };
    }

    @Override
    public NameInfo parse(StringReader reader) throws CommandSyntaxException {
        // Read the entire remaining input as the name (greedy final argument) so that
        // parameterized names containing '|', ':' or '%' round-trip from the suggestions.
        String remaining = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return NameInfo.parse(remaining);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (NameInfo info : getDataRepository().keySet()) {
            builder.suggest(info.compile());
        }
        return builder.buildFuture();
    }

    abstract DataRepository<T> getDataRepository();
}
