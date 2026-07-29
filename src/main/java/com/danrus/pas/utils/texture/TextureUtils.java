package com.danrus.pas.utils.texture;

import com.danrus.pas.ModExecutor;
import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.impl.features.OverlayFeature;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.OverlayMessageManger;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.files.EncodeUtils;
import com.danrus.pas.utils.files.ImageIO;
import com.danrus.pas.utils.mc.Id;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


public final class TextureUtils {
    private static final Map<OverlayKey, CompletableFuture<Identifier>> OVERLAY_TEXTURE_CACHE =
            new ConcurrentHashMap<>();
    private static final Set<Identifier> MISSING_OVERLAYS = ConcurrentHashMap.newKeySet();

    private TextureUtils() {}

    public static CompletableFuture<Identifier> registerTexture(
            Path path,
            Identifier identifier,
            boolean remap
    ) {
        NativeImage image = ImageIO.read(path);
        if (image == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Failed to read texture from " + path)
            );
        }
        return registerTexture(image, identifier, remap);
    }

    public static CompletableFuture<Identifier> registerTexture(
            NativeImage image,
            Identifier identifier,
            boolean remap
    ) {
        NativeImage processedImage = image;
        if (remap) {
            processedImage = TextureProcessor.remapLegacySkin(image);
            if (processedImage == null) {
                return CompletableFuture.failedFuture(
                        new IllegalArgumentException("Texture is not a valid 64-pixel-wide player skin")
                );
            }
        }
        return registerTexture(processedImage, identifier);
    }

    public static CompletableFuture<Identifier> registerTexture(
            NativeImage image,
            Identifier identifier
    ) {
        if (image == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Cannot register a null image")
            );
        }

        CompletableFuture<Identifier> future = new CompletableFuture<>();
        Minecraft.getInstance().execute(() -> {
            DynamicTexture texture = null;
            try {
                //? if <=1.21.4 {
                /*texture = new DynamicTexture(image);
                *///?} else {
                texture = new DynamicTexture(identifier::toString, image);
                //?}
                Minecraft.getInstance().getTextureManager().register(identifier, texture);
                clearOverlayCacheFor(identifier);
                future.complete(identifier);
            } catch (Exception e) {
                if (texture != null) {
                    texture.close();
                } else {
                    image.close();
                }
                PlayerArmorStandsClient.LOGGER.warn("Failed to register texture: {}", identifier, e);
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public static void unregisterTexture(Identifier identifier) {
        if (!PlayerArmorStandsClient.MOD_ID.equals(identifier.getNamespace())) return;
        Minecraft.getInstance().execute(() ->
                Minecraft.getInstance().getTextureManager().release(identifier)
        );
        clearOverlayCacheFor(identifier);
    }

    public static void clearOverlayCacheFor(NameInfo info) {
        InfoTranslators translators = InfoTranslators.getInstance();
        clearOverlayCacheFor(translators.toIdentifier(SkinData.class, info));
        clearOverlayCacheFor(translators.toIdentifier(CapeData.class, info));
    }

    public static void clearOverlayCacheFor(Identifier source) {
        OVERLAY_TEXTURE_CACHE.entrySet().removeIf(entry -> {
            if (!entry.getKey().source().equals(source)) {
                return false;
            }

            releaseWhenReady(entry.getValue());
            return true;
        });
    }

    public static void clearOverlayCache() {
        OVERLAY_TEXTURE_CACHE.values().forEach(TextureUtils::releaseWhenReady);
        OVERLAY_TEXTURE_CACHE.clear();
        MISSING_OVERLAYS.clear();
    }

    /**
     * Returns a newly allocated image. The caller owns and must close it.
     */
    @Nullable
    public static NativeImage copyNativeImage(Identifier identifier) {
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(identifier);
        if (texture instanceof DynamicTexture dynamicTexture) {
            NativeImage pixels = dynamicTexture.getPixels();
            if (pixels == null) {
                return null;
            }

            NativeImage copy = new NativeImage(pixels.getWidth(), pixels.getHeight(), true);
            copy.copyFrom(pixels);
            return copy;
        }

        if (texture instanceof SimpleTexture resourceTexture) {
            try {
                return resourceTexture.loadContents(Minecraft.getInstance().getResourceManager()).image();
            } catch (Exception e) {
                PlayerArmorStandsClient.LOGGER.warn("Failed to load texture image for: {}", identifier, e);
            }
        }
        return null;
    }

    public static Identifier getOverlayedTexture(
            NameInfo info,
            Class<? extends DataHolder> holderType
    ) {
        OverlayFeature feature = info.getFeature(OverlayFeature.class);
        OverlayTarget target = OverlayTarget.from(holderType);
        Identifier source = target.getSource(info);

        if (feature == null || !feature.isEnabled()) {
            return source;
        }

        return getOverlayTexture(
                source,
                feature.getTexture(),
                feature.getBlend(),
                target
        );
    }

    private static Identifier getOverlayTexture(
            Identifier source,
            String overlay,
            int blendStrength,
            OverlayTarget target
    ) {
        Identifier material = Id.vanilla("textures/block/" + overlay + ".png");
        if (MISSING_OVERLAYS.contains(material)) {
            return source;
        }

        Optional<Resource> materialResource =
                Minecraft.getInstance().getResourceManager().getResource(material);
        if (materialResource.isEmpty()) {
            markOverlayMissing(overlay, material);
            return source;
        }

        OverlayKey key = new OverlayKey(
                source,
                material,
                Math.max(0, Math.min(100, blendStrength)),
                target
        );
        CompletableFuture<Identifier> result = OVERLAY_TEXTURE_CACHE.computeIfAbsent(
                key,
                ignored -> createOverlayTexture(key, materialResource.get())
        );

        if (!result.isDone()) {
            return source;
        }
        if (result.isCompletedExceptionally()) {
            return source;
        }
        return result.getNow(source);
    }

    private static CompletableFuture<Identifier> createOverlayTexture(
            OverlayKey key,
            Resource materialResource
    ) {
        NativeImage sourceImage = copyNativeImage(key.source());
        if (sourceImage == null) {
            return CompletableFuture.failedFuture(
                    new IllegalStateException("Unsupported source texture: " + key.source())
            );
        }

        try {
            return CompletableFuture.supplyAsync(
                            () -> processOverlayImage(key, materialResource, sourceImage),
                            ModExecutor.MAIN_EXECUTOR
                    )
                    .thenCompose(result -> registerTexture(result, overlayLocation(key)))
                    .whenComplete((identifier, throwable) -> {
                        if (throwable != null) {
                            PlayerArmorStandsClient.LOGGER.warn(
                                    "Failed to create overlay texture {} over {}",
                                    key.material(),
                                    key.source(),
                                    throwable
                            );
                        }
                    });
        } catch (RuntimeException exception) {
            sourceImage.close();
            return CompletableFuture.failedFuture(exception);
        }
    }

    private static NativeImage processOverlayImage(
            OverlayKey key,
            Resource materialResource,
            NativeImage sourceImage
    ) {
        try (sourceImage; InputStream input = materialResource.open();
             NativeImage materialImage = NativeImage.read(input)) {
            NativeImage result = TextureProcessor.applyMaterial(
                    sourceImage,
                    materialImage,
                    key.blendStrength() / 100.0F
            );

            if (key.target() == OverlayTarget.SKIN) {
                result = TextureProcessor.remapLegacySkin(result);
                if (result == null) {
                    throw new IllegalArgumentException(
                            "Overlay source is not a valid player skin");
                }
            }

            return result;
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Failed to process overlay texture "
                            + key.material() + " over " + key.source(),
                    exception);
        }
    }

    private static Identifier overlayLocation(OverlayKey key) {
        String hash = EncodeUtils.encodeToSha256(
                key.source() + "|" + key.material() + "|" + key.blendStrength() + "|" + key.target()
        );
        return Id.pas("generated/overlay/" + hash);
    }

    private static void markOverlayMissing(String overlay, Identifier material) {
        if (MISSING_OVERLAYS.add(material)) {
            OverlayMessageManger.getInstance().showOverlayNotFoundMessage(overlay);
        }
    }

    private static void releaseWhenReady(CompletableFuture<Identifier> future) {
        future.thenAccept(TextureUtils::releaseTexture);
    }

    private static void releaseTexture(Identifier identifier) {
        Minecraft.getInstance().execute(() ->
                Minecraft.getInstance().getTextureManager().release(identifier)
        );
    }

    private record OverlayKey(
            Identifier source,
            Identifier material,
            int blendStrength,
            OverlayTarget target
    ) {}

    private enum OverlayTarget {
        SKIN {
            @Override
            Identifier getSource(NameInfo info) {
                return PasManager.getInstance().getSkinTexture(info);
            }
        },
        CAPE {
            @Override
            Identifier getSource(NameInfo info) {
                return PasManager.getInstance().getCapeTexture(info);
            }
        };

        abstract Identifier getSource(NameInfo info);

        static OverlayTarget from(Class<? extends DataHolder> holderType) {
            if (holderType == SkinData.class) {
                return SKIN;
            }
            if (holderType == CapeData.class) {
                return CAPE;
            }
            throw new IllegalArgumentException(
                    "Unsupported holder type for overlayed texture: " + holderType
            );
        }
    }
}
