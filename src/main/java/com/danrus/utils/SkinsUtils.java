package com.danrus.utils;

import com.danrus.PASExecutor;
import com.danrus.PlayerArmorStands;
import com.danrus.api.MojangApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.PlayerSkinTextureDownloader;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

public class SkinsUtils {

    public static Path CACHE_DIR = FabricLoader.getInstance().getGameDir().resolve("cache/pas");
    public static Identifier DEFAULT_TEXTURE = Identifier.ofVanilla("textures/entity/player/wide/steve.png");

    public static void reloadSkinTexure(String name) {
        if (!CACHE_DIR.resolve(StringUtils.encodeToSha256(name)+".png").toFile().delete()) {
            MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("§aFailed to delete texture for " + name), false);
        } else {
            registerSkinTexture(CACHE_DIR.resolve(StringUtils.encodeToSha256(name)+".png"), name, Identifier.of("pas", "skins/" + StringUtils.encodeToSha256(name) + ".png"));
        }
        getSkinTexture(name);
    }

    public static Identifier getSkinTexture(String name) {
        if (PlayerArmorStands.modelDataCache.containsKey(name) && PlayerArmorStands.modelDataCache.get(name).status == ASModelData.DownloadStatus.COMPLETED) {
            ASModelData modelData = PlayerArmorStands.modelDataCache.get(name);
            return modelData.texture;
        }

        if (CACHE_DIR.resolve(StringUtils.encodeToSha256(name)+".png").toFile().exists()) {
            Identifier id = Identifier.of("pas", "skins/" + StringUtils.encodeToSha256(name) + ".png");
            registerSkinTexture(CACHE_DIR.resolve(StringUtils.encodeToSha256(name)+".png"), name, id);
            PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, id, true, ASModelData.DownloadStatus.COMPLETED));
            return id;
        }

        // Если скина нет в кэше, возвращаем стандартный и запускаем загрузку

//        ASModelData defaultData = new ASModelData(name, defaultTexture, false, ASModelData.DownloadStatus.IN_PROGRESS);
//        PlayerArmorStands.modelDataCache.put(name, defaultData);

        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.downloading", name).formatted(Formatting.BLUE), false);
        loadSkinAsync(name);
        return DEFAULT_TEXTURE;
    }

    public static void loadSkinAsync(String name) {
        PASExecutor.execute(() -> {

            if (!MojangApi.isValidUsername(name)) {
                MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.invalid_username", name).formatted(Formatting.RED), false);
                PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, DEFAULT_TEXTURE, false, ASModelData.DownloadStatus.FAILED));
                return;
            }

            MojangApi.getProfileDataByNameAsync(name).thenAccept(profile -> {
                if (profile == null) {
                    doFailure(name, new Exception("Profile not found for: " + name));
                }

                MojangApi.getTexturedDataByUUIDAsync(profile.id).thenAccept(texturedProfile -> {
                    try {
                        if (texturedProfile == null || texturedProfile.properties == null || texturedProfile.properties.isEmpty()) {
                            doFailure(name, new Exception("No textures found for: " + name));
                        }

                        String encodedData = texturedProfile.properties.getFirst().value;
                        String decodedData = StringUtils.decodeBase64(encodedData);
                        MojangApi.SkinData skinData = PlayerArmorStands.GSON.fromJson(decodedData, MojangApi.SkinData.class);
                        String url = skinData.textures.SKIN.url;

                        String encodedName = StringUtils.encodeToSha256(name);
                        Identifier id = Identifier.of("pas", "skins/" + encodedName);
                        PlayerSkinTextureDownloader.downloadAndRegisterTexture(
                                id,
                                CACHE_DIR.resolve(encodedName + ".png"),
                                url, true
                        );
                        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.download_success", name).formatted(Formatting.GREEN), false);
                        PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, id, true, ASModelData.DownloadStatus.COMPLETED));
                    } catch (Exception e) {
                        doFailure(name, e);
                    }
                });
            });
        });
    }

    private static void doFailure(String name, Exception e) {
        e.printStackTrace();
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.download_failed", name), false);
        PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, DEFAULT_TEXTURE, false, ASModelData.DownloadStatus.FAILED));
    }

    public static void registerSkinTexture(Path skinPath, String name, Identifier id) {
        try (InputStream inputStream = Files.newInputStream(skinPath)) {
            NativeImage image = remapTexture(NativeImage.read(inputStream));
            MinecraftClient.getInstance().getTextureManager().registerTexture(id, new NativeImageBackedTexture(image));
            PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, id, true, ASModelData.DownloadStatus.COMPLETED));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static NativeImage remapTexture(NativeImage image) {
        int i = image.getHeight();
        int j = image.getWidth();
        if (j == 64 && (i == 32 || i == 64)) {
            boolean bl = i == 32;
            if (bl) {
                NativeImage nativeImage = new NativeImage(64, 64, true);
                nativeImage.copyFrom(image);
                image.close();
                image = nativeImage;
                nativeImage.fillRect(0, 32, 64, 32, 0);
                nativeImage.copyRect(4, 16, 16, 32, 4, 4, true, false);
                nativeImage.copyRect(8, 16, 16, 32, 4, 4, true, false);
                nativeImage.copyRect(0, 20, 24, 32, 4, 12, true, false);
                nativeImage.copyRect(4, 20, 16, 32, 4, 12, true, false);
                nativeImage.copyRect(8, 20, 8, 32, 4, 12, true, false);
                nativeImage.copyRect(12, 20, 16, 32, 4, 12, true, false);
                nativeImage.copyRect(44, 16, -8, 32, 4, 4, true, false);
                nativeImage.copyRect(48, 16, -8, 32, 4, 4, true, false);
                nativeImage.copyRect(40, 20, 0, 32, 4, 12, true, false);
                nativeImage.copyRect(44, 20, -8, 32, 4, 12, true, false);
                nativeImage.copyRect(48, 20, -16, 32, 4, 12, true, false);
                nativeImage.copyRect(52, 20, -8, 32, 4, 12, true, false);
            }

            stripAlpha(image, 0, 0, 32, 16);
            if (bl) {
                stripColor(image, 32, 0, 64, 32);
            }

            stripAlpha(image, 0, 16, 64, 32);
            stripAlpha(image, 16, 48, 48, 64);
            return image;
        } else {
            image.close();
            throw new IllegalStateException("Discarding incorrectly sized (" + j + "x" + i + ") skin texture: " + image.toString());
        }
    }
    private static void stripColor(NativeImage image, int x1, int y1, int x2, int y2) {
        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                int k = image.getColorArgb(i, j);
                if (ColorHelper.getAlpha(k) < 128) {
                    return;
                }
            }
        }

        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                image.setColorArgb(i, j, image.getColorArgb(i, j) & 16777215);
            }
        }

    }
    private static void stripAlpha(NativeImage image, int x1, int y1, int x2, int y2) {
        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                image.setColorArgb(i, j, ColorHelper.fullAlpha(image.getColorArgb(i, j)));
            }
        }

    }
}
