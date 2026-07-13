package com.danrus.pas.utils;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
//? if <1.21.9 {
//?}
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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

    public static Component getCustomName(ArmorStandRenderState armorStandState) {
        //? <1.21.9 {
        return armorStandState.customName;
        //?} else {
        /*return ((com.danrus.pas.extenders.ArmorStandRenderStateExtender) armorStandState).pas$getCustomName();
        *///?}
    }

    public static void setCustomName(ArmorStandRenderState armorStandState, Component name) {
        //? <1.21.9 {
        armorStandState.customName = name;
        //?} else {
        /*((com.danrus.pas.extenders.ArmorStandRenderStateExtender) armorStandState).pas$setCustomName(name);
         *///?}
    }

    public static int getARGBwhite(float alpha) {
        return (int) Math.floor(alpha * 255.0F) << 24 | 16777215;
    }

    public static ResourceLocation getPlayerSkinTexture(AbstractClientPlayer player){
        //?} if >1.20.1 && <1.21.9 {
        return player.getSkin().texture();
        //?} else {
        /*return player.getSkin().body().texturePath();
        *///?}
    }

    public static ResourceLocation getPlayerCapeTexture(AbstractClientPlayer player){
        //?} else if >1.20.1 && <1.21.9 {
        return player.getSkin().capeTexture();
        //?} else {
        /*try {
            return player.getSkin().cape().texturePath();
        } catch (Exception e) {
            return CapeData.DEFAULT_TEXTURE;
        }
        *///?}
    }

    public static void copyPartPose(ModelPart from, ModelPart to){
        //? <1.21.9 {
        to.copyFrom(from);
        //?} else {
        /*to.x = from.x;
        to.y = from.y;
        to.z = from.z;
        to.xRot = from.xRot;
        to.yRot = from.yRot;
        to.zRot = from.zRot;
        to.xScale = from.xScale;
        to.yScale = from.yScale;
        to.zScale = from.zScale;
        *///?}
    }

}
