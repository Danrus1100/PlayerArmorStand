package com.danrus.utils;

import com.danrus.PlayerArmorStands;
import com.danrus.api.MojangApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.PlayerSkinTextureDownloader;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

public class SkinsUtils {

    public static Path CACHE_DIR = FabricLoader.getInstance().getGameDir().resolve("cache/pas");

//    public static Identifier getSkinTexture(ASModelData data) {
//        if (!PlayerArmorStands.modelDataCache.containsKey(data.name)) {
//            getSkinTexture(data.name);
//        }
//        if (data.status == ASModelData.DownloadStatus.FAILED) {
//            return Identifier.ofVanilla("textures/entity/player/wide/steve.png");
//        }
//        return data.texture;
//    }

    public static Identifier getSkinTexture(String name) throws ExecutionException, InterruptedException {
        //TODO: Implement
        //Данный метод должен:
        //1. Качать скин если необходимо
        //2. Искать нужну текстуру в кэше
        String encodedName = StringUtils.encodeToSha256(name) + ".png";
        Path skinPath = CACHE_DIR.resolve(encodedName);
        boolean exists = skinPath.toFile().exists();
//        if (exists && !PlayerArmorStands.modelDataCache.containsKey(name)) {
//            Identifier id = Identifier.of("pas", "skins/" + encodedName);
//            try {
//                InputStream inputStream = Files.newInputStream(skinPath);
//                NativeImage image = NativeImage.read(inputStream);
//                MinecraftClient.getInstance().getTextureManager().registerTexture(id, new NativeImageBackedTexture(image));
//                PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, id, false, ASModelData.DownloadStatus.COMPLETED));
//            } catch (Exception e) {
//                e.printStackTrace();
//                try {
//                    Files.deleteIfExists(skinPath);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                exists = false;
//            }
//            return id;
//        }
        if (exists && !PlayerArmorStands.modelDataCache.containsKey(name)) {
            Identifier id = Identifier.of("pas", "skins/" + encodedName);
            MinecraftClient.getInstance().send(registerSkinTexture(skinPath, name, id));
            return id;
        }

        if (PlayerArmorStands.modelDataCache.containsKey(name)) {
            ASModelData modelData = PlayerArmorStands.modelDataCache.get(name);
            if (modelData.status == ASModelData.DownloadStatus.COMPLETED) {
                return modelData.texture;
            } else if (modelData.status == ASModelData.DownloadStatus.FAILED) {
                return  Identifier.ofVanilla("textures/entity/player/wide/steve.png");
            }
        }

        if (MojangApi.isValidUsername(name)) {
            if (!exists) {
                String url = MojangApi.getSkinUrl(name);
                if (url == null) {
                    PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, Identifier.ofVanilla("textures/entity/player/wide/steve.png"), false, ASModelData.DownloadStatus.FAILED));
                    return Identifier.ofVanilla("textures/entity/player/wide/steve.png");
                }
                MinecraftClient.getInstance().send(downloadSkin(url, name)); // Передаём исходное имя
            }
            return Identifier.of("pas", "skins/" + encodedName);
        }
        // Если имя невалидное, возвращаем скин по умолчанию
        PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, Identifier.ofVanilla("textures/entity/player/wide/steve.png"), false, ASModelData.DownloadStatus.FAILED));
        return Identifier.ofVanilla("textures/entity/player/wide/steve.png");
    }

    public static Runnable downloadSkin(String source, String name) {
        return () -> {
            String encodedName = StringUtils.encodeToSha256(name);
            Identifier id = Identifier.of("pas", "skins/" + encodedName);
            PlayerSkinTextureDownloader.downloadAndRegisterTexture(
                    id,
                    CACHE_DIR.resolve(encodedName + ".png"),
                    source, false
            );
            PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, id, false, ASModelData.DownloadStatus.COMPLETED));
        };

    }

    public static Runnable registerSkinTexture(Path skinPath, String name, Identifier id) {
        return () -> {
            try (InputStream inputStream = Files.newInputStream(skinPath)) {
                NativeImage image = NativeImage.read(inputStream);
                MinecraftClient.getInstance().getTextureManager().registerTexture(id, new NativeImageBackedTexture(image));
                PlayerArmorStands.modelDataCache.put(name, new ASModelData(name, id, false, ASModelData.DownloadStatus.COMPLETED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
