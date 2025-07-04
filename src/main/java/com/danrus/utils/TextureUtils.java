package com.danrus.utils;

import com.danrus.PASModelData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class TextureUtils {
    public static CompletableFuture<Identifier> registerTexture(Path path, Identifier identifier, boolean remap){
        try (InputStream inputStream = Files.newInputStream(path)) {
            NativeImage image = NativeImage.read(inputStream);
            return registerTexture(image, identifier, remap);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(MissingSprite.getMissingSpriteId());
        }
    }

    public static CompletableFuture<Identifier> registerTexture(NativeImage image, Identifier identifier, boolean remap) {
        CompletableFuture<Identifier> future = new CompletableFuture<>();

        try {
            if (remap) {
                image = remapTexture(image);
            }

            NativeImage finalImage = image;
            MinecraftClient.getInstance().execute(() -> {
                try {
                    //? <=1.21.4 {
                    /*NativeImageBackedTexture texture = new NativeImageBackedTexture(finalImage);
                    *///?} else {
                    NativeImageBackedTexture texture = new NativeImageBackedTexture(identifier::toString, finalImage);
                     //?}
                    MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, texture);
                    future.complete(identifier);
                } catch (Exception e) {
                    future.complete(PASModelData.DEFAULT_TEXTURE);
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            future.complete(PASModelData.DEFAULT_TEXTURE);
        }

        return future;
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
            return null;
        }
    }

    private static void stripColor(NativeImage image, int x1, int y1, int x2, int y2) {
        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                //? >=1.21.2 {
                int k = image.getColorArgb(i, j);
                //?} else {
                /*int k = image.getColor(i, j);
                *///?}
                if ((k >> 24 & 255) < 128) {
                    return;
                }
            }
        }

        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                //? >=1.21.2 {
                image.setColorArgb(i, j, image.getColorArgb(i, j) & 16777215);
                //?} else {
                /*image.setColor(i, j, image.getColor(i, j) & 16777215);
                 *///?}

            }
        }
    }
    private static void stripAlpha(NativeImage image, int x1, int y1, int x2, int y2) {
        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                //? >=1.21.2 {
                image.setColorArgb(i, j, image.getColorArgb(i, j) | -16777216);
                //?} else {
                /*image.setColor(i, j, image.getColor(i, j) | -16777216);
                 *///?}

            }
        }
    }
}
