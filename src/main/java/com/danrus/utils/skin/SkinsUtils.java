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
        } else {
            PASSkinDownloader.registerSkin(CACHE_DIR.resolve(StringUtils.encodeToSha256(name)+".png"), name, Identifier.of("pas", "skins/" + StringUtils.encodeToSha256(name) + ".png"));
        }
        getSkinTexture(name);
    }

    public static Identifier getSkinTexture(String name) {

//        if (MinecraftClient.getInstance().getTextureManager().getTexture(
//                Identifier.of("pas", "skins/" + StringUtils.encodeToSha256(name) + ".png")
//        ) instanceof MissingSprite) {
//            return DEFAULT_TEXTURE; //TODO антифиолет не работает, поспи сначала а потом напиши
//        }

        if (PlayerArmorStands.modelDataCache.containsKey(name) && PlayerArmorStands.modelDataCache.get(name).status.isCompleted()) {
            PASModelData modelData = PlayerArmorStands.modelDataCache.get(name);
            return modelData.texture;
        }

        String encodedName = StringUtils.encodeToSha256(name);

        if (CACHE_DIR.resolve(encodedName + "_cape.png").toFile().exists()) {
            Identifier capeId = Identifier.of("pas", "capes/" + encodedName + ".png");
            PASSkinDownloader.registerCape(CACHE_DIR.resolve(encodedName + "_cape.png"), name, capeId);
            PASModelData.registerCape(name, capeId);
        }

        if (CACHE_DIR.resolve(encodedName + ".png").toFile().exists()) {
            Identifier id = Identifier.of("pas", "skins/" + encodedName + ".png");
            PASSkinDownloader.registerSkin(CACHE_DIR.resolve(encodedName+".png"), name, id);
            PASModelData.registerCompleted(name, id);
            return id;
        }

        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.downloading", name).formatted(Formatting.BLUE), false);
        PASSkinDownloader.downloadSkin(name);
        return DEFAULT_TEXTURE;
    }


}
