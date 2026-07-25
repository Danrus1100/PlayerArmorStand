package com.danrus.pas.utils.texture;

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
import net.minecraft.resources.ResourceLocation;
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
    private static final Map<OverlayKey, CompletableFuture<ResourceLocation>> OVERLAY_TEXTURE_CACHE =
            new ConcurrentHashMap<>();
    private static final Set<ResourceLocation> MISSING_OVERLAYS = ConcurrentHashMap.newKeySet();

    private TextureUtils() {}

    public static CompletableFuture<ResourceLocation> registerTexture(
            Path path,
            ResourceLocation identifier,
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

    public static CompletableFuture<ResourceLocation> registerTexture(
            NativeImage image,
            ResourceLocation identifier,
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

    public static CompletableFuture<ResourceLocation> registerTexture(
            NativeImage image,
            ResourceLocation identifier
    ) {
        if (image == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Cannot register a null image")
            );
        }

        CompletableFuture<ResourceLocation> future = new CompletableFuture<>();
        Minecraft.getInstance().execute(() -> {
            DynamicTexture texture = null;
            try {
                //? if <=1.21.4 {
                /*texture = new DynamicTexture(image);
                *///?} else {
                texture = new DynamicTexture(identifier::toString, image);
                //?}
                Minecraft.getInstance().getTextureManager().register(identifier, texture);
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

    public static void unregisterTexture(ResourceLocation identifier) {
        Minecraft.getInstance().execute(() ->
                Minecraft.getInstance().getTextureManager().release(identifier)
        );
        clearOverlayCacheFor(identifier);
    }

    public static void clearOverlayCacheFor(NameInfo info) {
        InfoTranslators translators = InfoTranslators.getInstance();
        clearOverlayCacheFor(translators.toResourceLocation(SkinData.class, info));
        clearOverlayCacheFor(translators.toResourceLocation(CapeData.class, info));
    }

    public static void clearOverlayCacheFor(ResourceLocation source) {
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
    public static NativeImage copyNativeImage(ResourceLocation identifier) {
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
                //? if <=1.21.1 {
                /*return resourceTexture.getTextureImage(Minecraft.getInstance().getResourceManager()).getImage();
                *///?} else {
                return resourceTexture.loadContents(Minecraft.getInstance().getResourceManager()).image();
                //?}
            } catch (Exception e) {
                PlayerArmorStandsClient.LOGGER.warn("Failed to load texture image for: {}", identifier, e);
            }
        }
        return null;
    }

    public static ResourceLocation getOverlayedTexture(
            NameInfo info,
            Class<? extends DataHolder> holderType
    ) {
        OverlayFeature feature = info.getFeature(OverlayFeature.class);
        OverlayTarget target = OverlayTarget.from(holderType);
        ResourceLocation source = target.getSource(info);

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

    private static ResourceLocation getOverlayTexture(
            ResourceLocation source,
            String overlay,
            int blendStrength,
            OverlayTarget target
    ) {
        ResourceLocation material = Id.vanilla("textures/block/" + overlay + ".png");
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
        CompletableFuture<ResourceLocation> result = OVERLAY_TEXTURE_CACHE.computeIfAbsent(
                key,
                ignored -> createOverlayTexture(key, materialResource.get())
        );

        if (!result.isDone()) {
            return source;
        }
        if (result.isCompletedExceptionally()) {
            OVERLAY_TEXTURE_CACHE.remove(key, result);
            return source;
        }
        return result.getNow(source);
    }

    private static CompletableFuture<ResourceLocation> createOverlayTexture(
            OverlayKey key,
            Resource materialResource
    ) {
        NativeImage sourceImage = copyNativeImage(key.source());
        if (sourceImage == null) {
            return CompletableFuture.failedFuture(
                    new IllegalStateException("Unsupported source texture: " + key.source())
            );
        }

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
                    return CompletableFuture.failedFuture(
                            new IllegalArgumentException("Overlay source is not a valid player skin")
                    );
                }
            }

            ResourceLocation output = overlayLocation(key);
            return registerTexture(result, output).whenComplete((identifier, throwable) -> {
                if (throwable != null) {
                    OVERLAY_TEXTURE_CACHE.remove(key);
                    PlayerArmorStandsClient.LOGGER.warn(
                            "Failed to create overlay texture {} over {}",
                            key.material(),
                            key.source(),
                            throwable
                    );
                }
            });
        } catch (Exception e) {
            PlayerArmorStandsClient.LOGGER.warn(
                    "Failed to process overlay texture {} over {}",
                    key.material(),
                    key.source(),
                    e
            );
            return CompletableFuture.failedFuture(e);
        }
    }

    private static ResourceLocation overlayLocation(OverlayKey key) {
        String hash = EncodeUtils.encodeToSha256(
                key.source() + "|" + key.material() + "|" + key.blendStrength() + "|" + key.target()
        );
        return Id.pas("generated/overlay/" + hash);
    }

    private static void markOverlayMissing(String overlay, ResourceLocation material) {
        if (MISSING_OVERLAYS.add(material)) {
            OverlayMessageManger.getInstance().showOverlayNotFoundMessage(overlay);
        }
    }

    private static void releaseWhenReady(CompletableFuture<ResourceLocation> future) {
        future.thenAccept(TextureUtils::releaseTexture);
    }

    private static void releaseTexture(ResourceLocation identifier) {
        Minecraft.getInstance().execute(() ->
                Minecraft.getInstance().getTextureManager().release(identifier)
        );
    }

    private record OverlayKey(
            ResourceLocation source,
            ResourceLocation material,
            int blendStrength,
            OverlayTarget target
    ) {}

    private enum OverlayTarget {
        SKIN {
            @Override
            ResourceLocation getSource(NameInfo info) {
                return PasManager.getInstance().getSkinTexture(info);
            }
        },
        CAPE {
            @Override
            ResourceLocation getSource(NameInfo info) {
                return PasManager.getInstance().getCapeTexture(info);
            }
        };

        abstract ResourceLocation getSource(NameInfo info);

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
