package com.danrus.managers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class OverlayMessageManger {

    private static OverlayMessageManger INSTANCE = new OverlayMessageManger();

    public void showDownloadMessage(String name) {
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.downloading", name).formatted(Formatting.BLUE), false);
    }

    public void showInvalidNameMessage(String name) {
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.invalid_username", name).formatted(Formatting.RED), false);
    }

    public void showSuccessMessage(String name) {
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.download_success", name).formatted(Formatting.GREEN), false);
    }

    public void showFailMessage(String name) {
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.download_failed", name).formatted(Formatting.RED), false);
    }

    public static OverlayMessageManger getInstance() {
        return INSTANCE;
    }
}
