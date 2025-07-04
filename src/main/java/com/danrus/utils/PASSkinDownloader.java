package com.danrus.utils;

import com.danrus.PASExecutor;
import com.danrus.PASModelData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
//import net.minecraft.util.PathUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class PASSkinDownloader {
//    public static void downloadSkin(String username, String params) {
//        PASExecutor.execute(() -> {
//
//                TextureHandler.getTextureURLS(username, source).thenApply(urls -> {
//
//                if (urls == null || urls.isEmpty()) {
//                    OverlayMessagesManager.doFailure(username, new Exception("No skin URLs found for: " + username));
//                    return null;
//                }
//
////                String filename = StringUtils.encodeToSha256(username);
//                String filename = source == "namemc" ? username + "_namemc" : StringUtils.encodeToSha256(username);
//                Identifier id = Identifier.of("pas", "skins/" + filename);
//
//                downloadAndRegister(
//                        id,
//                        SkinsUtils.CACHE_DIR.resolve(filename + ".png"),
//                        urls.get(0), true
//                );
//
//                String capeUrl = urls.size() > 1 ? urls.get(1) : null;
//                if (capeUrl != null && !capeUrl.isEmpty()) {
//                    Identifier capeId = Identifier.of("pas", "capes/" + filename);
//                    downloadAndRegister(
//                            capeId,
//                            SkinsUtils.CACHE_DIR.resolve(filename + "_cape.png"),
//                            capeUrl, false
//                    ).thenAccept(
//                            capeTextureId -> PASModelData.registerCape(username, capeTextureId)
//                    );
//                }
//
//                MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.download_success", username).formatted(Formatting.GREEN), false);
//                PASModelData.registerCompleted(username, id);
//                return null;
//            });
//        });
//    }

    public static CompletableFuture<Identifier> downloadAndRegister(Identifier textureId, Path path, String uri, boolean remap) {
        return CompletableFuture.supplyAsync(() -> {
            NativeImage nativeImage;
            try {
                nativeImage = download(path, uri);
            } catch (IOException iOException) {
                throw new UncheckedIOException(iOException);
            }
            return nativeImage;
        }, PASExecutor.DOWNLOAD_EXECUTOR).thenCompose(image -> TextureUtils.registerTexture(image, textureId, remap));
    }

    private static NativeImage download(Path path, String uri) throws IOException {
        // Сначала проверяем кэшированный файл
        if (Files.isRegularFile(path, new LinkOption[0])) {
            try (InputStream inputStream = Files.newInputStream(path)) {
                return NativeImage.read(inputStream);
            }
        }

        // Если файл не найден в кэше, загружаем его
        int maxRetries = 3;
        int currentTry = 0;
        IOException lastException = null;

        while (currentTry < maxRetries) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) URI.create(uri)
                    .toURL()
                    .openConnection(MinecraftClient.getInstance().getNetworkProxy());

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
