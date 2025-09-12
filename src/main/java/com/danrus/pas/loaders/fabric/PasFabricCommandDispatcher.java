package com.danrus.pas.loaders.fabric;

import com.danrus.pas.utils.commands.PasCommandDispatcher;
import com.danrus.pas.utils.commands.PasCommandSource;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class PasFabricCommandDispatcher implements PasCommandDispatcher {

    private final CommandDispatcher<FabricClientCommandSource> dispatcher;

    public PasFabricCommandDispatcher(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void register(LiteralArgumentBuilder<PasCommandSource> command) {
        dispatcher.register(adapt(command));
    }

    private LiteralArgumentBuilder<FabricClientCommandSource> adapt(LiteralArgumentBuilder<PasCommandSource> builder) {
        LiteralArgumentBuilder<FabricClientCommandSource> result = LiteralArgumentBuilder.literal(builder.getLiteral());
        adapt(builder, result);
        return result;
    }

    private <T extends ArgumentBuilder<PasCommandSource, T>> void adapt(ArgumentBuilder<PasCommandSource, T> source, ArgumentBuilder<FabricClientCommandSource, ?> target) {
        if (source.getCommand() != null) {
            target.executes(context -> source.getCommand().run(context.copyFor(new PasFabricCommandSourceImpl(context.getSource()))));
        }
        if (source.getRequirement() != null) {
            target.requires(source1 -> source.getRequirement().test(new PasFabricCommandSourceImpl(source1)));
        }
        // Рекурсивно адаптируем все дочерние узлы
        for (CommandNode<PasCommandSource> childNode : source.getArguments()) {
            target.then(adaptNode(childNode));
        }
    }

    private ArgumentBuilder<FabricClientCommandSource, ?> adaptNode(CommandNode<PasCommandSource> node) {
        // createBuilder() создает правильный тип ArgumentBuilder (Literal, Required, etc.)
        @SuppressWarnings("unchecked")
        ArgumentBuilder<PasCommandSource, ?> sourceBuilder = node.createBuilder();
        @SuppressWarnings("unchecked")
        ArgumentBuilder<FabricClientCommandSource, ?> targetBuilder = node.createBuilder();

        adapt(sourceBuilder, targetBuilder);
        return targetBuilder;
    }
}
