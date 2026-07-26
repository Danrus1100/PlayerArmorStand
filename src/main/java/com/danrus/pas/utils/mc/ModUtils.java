package com.danrus.pas.utils.mc;

import com.danrus.pas.impl.holder.CapeData;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
//? if <1.21.9 {
//?}
import net.minecraft.resources.Identifier;

import java.nio.file.Path;

public class ModUtils {
    public static String YACL_MOD_ID = "yet_another_config_lib_v3";
    public static String MOD_ID = "pas";

    public static Path getGameDir() {
        //? if fabric {
        return net.fabricmc.loader.api.FabricLoader.getInstance().getGameDir();
        //?} else if neoforge {
        /*return net.neoforged.fml.loading.FMLPaths.GAMEDIR.get();
        *///?} else if forge {
        /*return net.minecraftforge.fml.loading.FMLPaths.GAMEDIR.get();
        *///?}
    }

    public static boolean isModLoaded(String modId) {
        //? if fabric {
        return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded(modId);
        //?} else if neoforge && <=1.21.8 {
        /*return net.neoforged.fml.loading.FMLLoader.getLoadingModList().getModFileById(modId) != null;
        *///?} else if neoforge && >=1.21.10 {
        /*return net.neoforged.fml.loading.FMLLoader.getCurrent().getLoadingModList().getModFileById(modId) != null;
        *///?} else if forge {
        /*return net.minecraftforge.fml.ModList.get().isLoaded(modId);
        *///?}
    }

    public static int getARGBwhite(float alpha) {
        return (int) Math.floor(alpha * 255.0F) << 24 | 16777215;
    }

    public static Identifier getPlayerCapeTextureSafe(AbstractClientPlayer player){
        try {
            return player.getSkin().cape().texturePath();
        } catch (Exception e) {
            return CapeData.DEFAULT_TEXTURE;
        }
    }

    public static void copyPartPose(ModelPart from, ModelPart to){
        to.x = from.x;
        to.y = from.y;
        to.z = from.z;
        to.xRot = from.xRot;
        to.yRot = from.yRot;
        to.zRot = from.zRot;
        to.xScale = from.xScale;
        to.yScale = from.yScale;
        to.zScale = from.zScale;
    }

}
