package com.danrus.utils.skin;

import com.danrus.PlayerArmorStands;
import com.danrus.utils.PASModelData;
import com.danrus.utils.StringUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.nio.file.Path;

public class SkinsUtils {
    public static Path CACHE_DIR = FabricLoader.getInstance().getGameDir().resolve("cache/pas");
    public static Identifier DEFAULT_TEXTURE = Identifier.of("minecraft", "textures/entity/player/wide/steve.png");

    public static void reloadSkinTexure(String name) {
        if (!CACHE_DIR.resolve(StringUtils.encodeToSha256(name)+".png").toFile().delete()) {
            MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("§aFailed to delete texture for " + name), false);
        }
        if (!CACHE_DIR.resolve(name + "_cape.png").toFile().delete()) {
            MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("§aFailed to delete cape texture for " + name), false);
        }

        if (PlayerArmorStands.modelDataCache.containsKey(name)) {
            PlayerArmorStands.modelDataCache.remove(name);
        }

        if (CACHE_DIR.resolve(name + "_namemc.png").toFile().delete() || PlayerArmorStands.modelDataCache.containsKey(name)) {
//            MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("§aFailed to delete namemc texture for " + name), false);
            name = name + "|N";
        }
        getSkinTexture(name);
    }

    public static Identifier getSkinTexture(String string) {

        String name = StringUtils.matchASName(string).get(0);
        boolean isNameMC = StringUtils.matchASName(string).get(1).contains("N");

//        if (MinecraftClient.getInstance().getTextureManager().getTexture(
//                Identifier.of("pas", "skins/" + StringUtils.encodeToSha256(name) + ".png")
//        ) instanceof MissingSprite) {
//            return DEFAULT_TEXTURE; //FIXME антифиолет не работает
//        }

        if (PlayerArmorStands.modelDataCache.containsKey(name) && PlayerArmorStands.modelDataCache.get(name).status.isCompleted()) {
            PASModelData modelData = PlayerArmorStands.modelDataCache.get(name);
            return modelData.texture;
        }

        String filename = isNameMC ? name + "_namemc" : StringUtils.encodeToSha256(name);

        if (CACHE_DIR.resolve(filename + ".png").toFile().exists()) {
            Identifier id = Identifier.of("pas", "skins/" + filename + ".png");
            PASSkinDownloader.registerSkin(CACHE_DIR.resolve(filename+".png"), name, id);
            PASModelData.registerCompleted(name, id);

            if (CACHE_DIR.resolve(filename + "_cape.png").toFile().exists()) {
                Identifier capeId = Identifier.of("pas", "capes/" + filename + ".png");
                PASSkinDownloader.registerCape(CACHE_DIR.resolve(filename + "_cape.png"), name, capeId);
                PASModelData.registerCape(name, capeId);
            }
            
            return id;
        }

        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.downloading", name).formatted(Formatting.BLUE), false);
        if (isNameMC) {
            PASSkinDownloader.downloadSkin(name, "namemc");
        } else {
            PASSkinDownloader.downloadSkin(name, "mojang");
        }
        return DEFAULT_TEXTURE;
    }


}
