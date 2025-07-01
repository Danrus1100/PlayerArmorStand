package com.danrus;

import com.danrus.utils.PASModelData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

//TODO: implement
public class OverlayMessagesManager {

    public static void doFailure(String name, Exception e) {
        e.printStackTrace();
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.download_failed", name).formatted(Formatting.RED), false);
        PASModelData.registerFailed(name);
    }
}
