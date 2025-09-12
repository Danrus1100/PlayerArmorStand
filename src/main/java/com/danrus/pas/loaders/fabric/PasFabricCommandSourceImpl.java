package com.danrus.pas.loaders.fabric;

import com.danrus.pas.utils.commands.PasCommandSource;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class PasFabricCommandSourceImpl implements PasCommandSource, FabricClientCommandSource {

    private FabricClientCommandSource source;

    PasFabricCommandSourceImpl(FabricClientCommandSource source) {
        this.source = source;
    }

    @Override
    public void fail(Component message) {
        sendError(message);
    }

    @Override
    public void feedback(Component message) {
        sendFeedback(message);
    }

    @Override
    public void sendFeedback(Component message) {
        source.sendFeedback(message);
    }

    @Override
    public void sendError(Component message) {
        source.sendError(message);
    }

    @Override
    public Minecraft getClient() {
        return source.getClient();
    }

    @Override
    public LocalPlayer getPlayer() {
        return source.getPlayer();
    }

    @Override
    public ClientLevel getWorld() {
        return source.getWorld();
    }

    @Override
    public Collection<String> getOnlinePlayerNames() {
        return source.getOnlinePlayerNames();
    }

    @Override
    public Collection<String> getAllTeams() {
        return source.getAllTeams();
    }

    @Override
    public Stream<ResourceLocation> getAvailableSounds() {
        return source.getAvailableSounds();
    }

    @Override
    public CompletableFuture<Suggestions> customSuggestion(CommandContext<?> context) {
        return source.customSuggestion(context);
    }

    @Override
    public Set<ResourceKey<Level>> levels() {
        return source.levels();
    }

    @Override
    public RegistryAccess registryAccess() {
        return source.registryAccess();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return source.enabledFeatures();
    }

    @Override
    public CompletableFuture<Suggestions> suggestRegistryElements(ResourceKey<? extends Registry<?>> resourceKey, ElementSuggestionType registryKey, SuggestionsBuilder builder, CommandContext<?> context) {
        return source.suggestRegistryElements(resourceKey, registryKey, builder, context);
    }

    @Override
    public boolean hasPermission(int permissionLevel) {
        return source.hasPermission(permissionLevel);
    }
}
