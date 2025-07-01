package com.danrus.api;

import com.danrus.OverlayMessagesManager;
import com.danrus.PlayerArmorStands;
import com.danrus.utils.PASModelData;
import com.danrus.utils.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TextureHandler {
    public static CompletableFuture<List<String>> getTextureURLS(String username, String source) {
        if (source == "mojang") {
            if (!MojangApi.isValidUsername(username)) {
                MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.invalid_username", username).formatted(Formatting.RED), false);
                PASModelData.registerFailed(username);
                return CompletableFuture.completedFuture(null);
            }
            return MojangApi.getProfileDataByNameAsync(username).thenCompose(profile -> {
                if (profile == null) {
                    OverlayMessagesManager.doFailure(username, new Exception("Profile not found for: " + username));
                    return CompletableFuture.completedFuture(null);
                }

                return MojangApi.getTexturedDataByUUIDAsync(profile.id).thenApply(texturedProfile -> {
                    try {
                        if (texturedProfile == null || texturedProfile.properties == null || texturedProfile.properties.isEmpty()) {
                            OverlayMessagesManager.doFailure(username, new Exception("No textures found for: " + username));
                            return null;
                        }

                        String encodedData = texturedProfile.properties.get(0).value;
                        String decodedData = StringUtils.decodeBase64(encodedData);
                        MojangApi.SkinData skinData = PlayerArmorStands.GSON.fromJson(decodedData, MojangApi.SkinData.class);
                        return List.of(
                                skinData.textures.SKIN.url,
                                skinData.textures.CAPE != null ? skinData.textures.CAPE.url : ""
                        );
                    } catch (Exception e) {
                        OverlayMessagesManager.doFailure(username, e);
                        return null;
                    }
                }).exceptionally(e -> {
                    OverlayMessagesManager.doFailure(username, new Exception("Failed to fetch textured data for: " + username, e));
                    return null;
                });
            });
        } else if (source == "namemc") {
            return NamemcApi.getSkinUrl(username)
                    .thenApply(url -> {
                        if (url == null) {
                            OverlayMessagesManager.doFailure(username, new Exception("No skin found for: " + username));
                            return null;
                        }
                        return List.of(url);
                    }).exceptionally(e -> {
                        OverlayMessagesManager.doFailure(username, new Exception("Failed to fetch skin URL for: " + username, e));
                        return null;
                    });
        }
        OverlayMessagesManager.doFailure(username, new Exception("Invalid source: " + source));
        return null;
    }
}

