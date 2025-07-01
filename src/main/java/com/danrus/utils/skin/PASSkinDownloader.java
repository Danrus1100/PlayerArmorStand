package com.danrus.utils.skin;

import com.danrus.OverlayMessagesManager;
import com.danrus.PASExecutor;
import com.danrus.PlayerArmorStands;
import com.danrus.api.MojangApi;
import com.danrus.api.TextureHandler;
import com.danrus.utils.PASModelData;
import com.danrus.utils.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
//import net.minecraft.util.PathUtil;
import net.minecraft.util.math.ColorHelper;

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
    public static void downloadSkin(String username, String source) {
        PASExecutor.execute(() -> {

                TextureHandler.getTextureURLS(username, source).thenApply(urls -> {

                if (urls == null || urls.isEmpty()) {
                    OverlayMessagesManager.doFailure(username, new Exception("No skin URLs found for: " + username));
                    return null;
                }

//                String filename = StringUtils.encodeToSha256(username);
                String filename = source == "namemc" ? username + "_namemc" : StringUtils.encodeToSha256(username);
                Identifier id = Identifier.of("pas", "skins/" + filename);

                downloadAndRegisterSkin(
                        id,
                        SkinsUtils.CACHE_DIR.resolve(filename + ".png"),
                        urls.get(0), true
                );

                String capeUrl = urls.size() > 1 ? urls.get(1) : null;
                if (capeUrl != null && !capeUrl.isEmpty()) {
                    Identifier capeId = Identifier.of("pas", "capes/" + filename);
                    downloadAndRegisterSkin(
                            capeId,
                            SkinsUtils.CACHE_DIR.resolve(filename + "_cape.png"),
                            capeUrl, false
                    ).thenAccept(
                            capeTextureId -> PASModelData.registerCape(username, capeTextureId)
                    );
                }

                MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("pas.download_success", username).formatted(Formatting.GREEN), false);
                PASModelData.registerCompleted(username, id);
                return null;
            });
        });
    }

    private static CompletableFuture<Identifier> downloadAndRegisterSkin(Identifier textureId, Path path, String uri, boolean remap){
        return CompletableFuture.supplyAsync(() -> {
            NativeImage nativeImage;
            try {
                nativeImage = download(path, uri);
            } catch (IOException iOException) {
                throw new UncheckedIOException(iOException);
            }

//            return /*? >=1.21.2 *//*remap ? remapTexture(nativeImage) :*//*} else {*/nativeImage/*?}*/;
//        }, Util.getDownloadWorkerExecutor()/*? >=1.21.2 *//*.named("downloadTexture")*//*?}*/).thenCompose((image) -> registerSkin(textureId, image));
            if (remap) {
                nativeImage = remapTexture(nativeImage);
            }
            return nativeImage;
        }, PASExecutor.DOWNLOAD_EXECUTOR).thenCompose((image) -> registerSkin(textureId, image));
    }
    private static NativeImage download(Path path, String uri) throws IOException {
        if (Files.isRegularFile(path, new LinkOption[0])) {

            try (InputStream inputStream = Files.newInputStream(path)) {
                return NativeImage.read(inputStream);
            }
        } else {
            HttpURLConnection httpURLConnection = null;
            URI uRI = URI.create(uri);

            NativeImage iOException;
            try {
                httpURLConnection = (HttpURLConnection)uRI.toURL().openConnection(MinecraftClient.getInstance().getNetworkProxy());
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(false);
                httpURLConnection.connect();
                int i = httpURLConnection.getResponseCode();
                if (i / 100 != 2) {
                    String var10002 = String.valueOf(uRI);
                    throw new IOException("Failed to open " + var10002 + ", HTTP error code: " + i);
                }

                byte[] bs = httpURLConnection.getInputStream().readAllBytes();

                try {
                    Files.write(path, bs, new OpenOption[0]);
                } catch (IOException var13) {
//                    LOGGER.warn("Failed to cache texture {} in {}", uri, path);
                }

                iOException = NativeImage.read(bs);
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

            }

            return iOException;
        }
    }

    private static CompletableFuture<Identifier> registerSkin(Identifier textureId, NativeImage image) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        return CompletableFuture.supplyAsync(() -> {
            //? <=1.21.4 {
            NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
            //?} else {
            /*NativeImageBackedTexture texture = new NativeImageBackedTexture(textureId::toString, image);
            *///?}
            minecraftClient.getTextureManager().registerTexture(textureId, texture);
            return textureId;
        }, minecraftClient);
    }

    public static void registerSkin(Path skinPath, String name, Identifier id) {
        try (InputStream inputStream = Files.newInputStream(skinPath)) {
            NativeImage image = remapTexture(NativeImage.read(inputStream));
//            NativeImage image = NativeImage.read(inputStream);
            //? <=1.21.4 {
            NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
             //?} else {
            /*NativeImageBackedTexture texture = new NativeImageBackedTexture(id::toString, image);
            *///?}
            MinecraftClient.getInstance().getTextureManager().registerTexture(id, texture);
            PASModelData.registerCompleted(name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerCape(Path capePath, String name, Identifier id) {
        try (InputStream inputStream = Files.newInputStream(capePath)) {
            NativeImage image = NativeImage.read(inputStream);
            //? <=1.21.4 {
            NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
             //?} else {
            /*NativeImageBackedTexture texture = new NativeImageBackedTexture(id::toString, image);
            *///?}
            MinecraftClient.getInstance().getTextureManager().registerTexture(id, texture);
            PASModelData.registerCape(name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //? if >=1.21.2 {
    /*private static NativeImage remapTexture(NativeImage image) {
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
            throw new IllegalStateException("Discarding incorrectly sized (" + j + "x" + i + ") skin texture: " + image.toString());
        }
    }
    private static void stripColor(NativeImage image, int x1, int y1, int x2, int y2) {
        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                int k = image.getColorArgb(i, j);
                if (ColorHelper.getAlpha(k) < 128) {
                    return;
                }
            }
        }

        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                image.setColorArgb(i, j, image.getColorArgb(i, j) & 16777215);
            }
        }

    }
    private static void stripAlpha(NativeImage image, int x1, int y1, int x2, int y2) {
        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                image.setColorArgb(i, j, ColorHelper.fullAlpha(image.getColorArgb(i, j)));
            }
        }

    }
    *///?} else {
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
                int k = image.getColor(i, j);
                if ((k >> 24 & 255) < 128) {
                    return;
                }
            }
        }

        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                image.setColor(i, j, image.getColor(i, j) & 16777215);
            }
        }

    }

    private static void stripAlpha(NativeImage image, int x1, int y1, int x2, int y2) {
        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j) {
                image.setColor(i, j, image.getColor(i, j) | -16777216);
            }
        }

    }
    //?}
}
