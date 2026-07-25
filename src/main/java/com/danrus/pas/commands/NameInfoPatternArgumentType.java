package com.danrus.pas.commands;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoPattern;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class NameInfoPatternArgumentType<T extends DataHolder> implements ArgumentType<NameInfoPattern> {

    public static NameInfoPattern getPattern(CommandContext<?> ctx, String name) {
        return ctx.getArgument(name, NameInfoPattern.class);
    }

    public static NameInfoPatternArgumentType<SkinData> forSkin() {
        return new NameInfoPatternArgumentType<>() {
            @Override
            DataRepository<SkinData> getDataRepository() {
                return PasManager.getInstance().getSkinDataManager();
            }
        };
    }

    public static NameInfoPatternArgumentType<CapeData> forCape() {
        return new NameInfoPatternArgumentType<>() {
            @Override
            DataRepository<CapeData> getDataRepository() {
                return PasManager.getInstance().getCapeDataManager();
            }
        };
    }

    @Override
    public NameInfoPattern parse(StringReader reader) throws CommandSyntaxException {
        // Read the entire remaining input as the name (greedy final argument) so that
        // parameterized names containing '|', ':' or '%' round-trip from the suggestions.
        String remaining = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());

        return NameInfoPattern.any().withBaseName(remaining);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> suggested = new ArrayList<>();
        for (NameInfo info : getDataRepository().allNames()) {
            NameInfoPattern pattern = info.toBaseNamePattern();
            String suggest = pattern.getBaseName().orElse(null);
            if (suggest == null) continue;
            if (!suggested.contains(suggest)) {
                builder.suggest(suggest);
                suggested.add(suggest);
            };
        }
        return builder.buildFuture();
    }

    abstract DataRepository<T> getDataRepository();
}
