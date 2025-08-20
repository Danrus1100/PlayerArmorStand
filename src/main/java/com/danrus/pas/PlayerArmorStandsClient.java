package com.danrus.pas;

import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.utils.managers.CommandsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerArmorStandsClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("PlayerArmorStands");

    public static final ResourceLocation SKIN_ATLAS = VersioningUtils.getResourceLocation("pas", "textures/atlas/skins.png");

    public static void initialize() {
        CommandsManager.init();
        TextureAtlas skinAtlas = new TextureAtlas(SKIN_ATLAS);
        Minecraft.getInstance().getTextureManager().register(SKIN_ATLAS, skinAtlas);
    }
}
