package com.danrus.pas.utils;

import com.danrus.pas.ModExecutor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class SkinDownloader {
    public static CompletableFuture<ResourceLocation> downloadAndRegister(ResourceLocation texture, Path path, String uri, boolean remap) {
    return CompletableFuture.supplyAsync(() -> {
        NativeImage nativeImage;
        try {
            nativeImage = download(path, uri);
        } catch (IOException iOException) {
            throw new UncheckedIOException(iOException);
        }
        return nativeImage;
    }, ModExecutor.DOWNLOAD_EXECUTOR).thenCompose(image -> TextureUtils.registerTexture(image, texture, remap));
}

    private static NativeImage download(Path path, String uri) throws IOException {
        if (Files.isRegularFile(path, new LinkOption[0])) {
            try (InputStream inputStream = Files.newInputStream(path)) {
                return NativeImage.read(inputStream);
            }
        }
        int maxRetries = 3;
        int currentTry = 0;
        IOException lastException = null;

        while (currentTry < maxRetries) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) URI.create(uri)
                        .toURL()
                        .openConnection(Minecraft.getInstance().getProxy());

                connection.setRequestProperty("User-Agent", "Mozilla/5.0 Minecraft Client");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    byte[] data = connection.getInputStream().readAllBytes();

                    // Пытаемся сохранить в кэш
                    try {
                        Files.write(path, data);
                    } catch (IOException e) {
                        // Игнорируем ошибки кэширования
                    }

                    return NativeImage.read(data);
                }

                currentTry++;
                Thread.sleep(1000); // Пауза между попытками

            } catch (IOException e) {
                lastException = e;
                currentTry++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Download interrupted", e);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Download interrupted");
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        throw new IOException("Failed to download after " + maxRetries + " attempts", lastException);
    }
}
