package com.danrus.pas.utils.files;

import com.danrus.pas.PlayerArmorStandsClient;
import com.mojang.blaze3d.platform.NativeImage;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ImageIO {
    private ImageIO(){}

    public static void saveImage(NativeImage image, Path path) {
        try {
            image.writeToFile(path.toFile());
        } catch (Exception e) {
            PlayerArmorStandsClient.LOGGER.warn("Failed to save texture to path: {}", path.toFile().getName(), e);
        }
    }

    @Nullable
    public static NativeImage read(Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return NativeImage.read(inputStream);
        } catch (Exception e) {
            PlayerArmorStandsClient.LOGGER.warn("Failed to read texture from path: {}", path, e);
            return null;
        }
    }
}
