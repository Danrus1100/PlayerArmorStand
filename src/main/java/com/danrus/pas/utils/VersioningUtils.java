package com.danrus.pas.utils;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.file.Path;
import java.util.function.Function;

public class VersioningUtils {
    public static Path getGameDir() {
        //? if fabric {
        return net.fabricmc.loader.api.FabricLoader.getInstance().getGameDir();
        //?} else if neoforge {
        /*return net.neoforged.fml.loading.FMLPaths.GAMEDIR.get();
        *///?} else if forge {
        /*return net.minecraftforge.fml.loading.FMLPaths.GAMEDIR.get();
        *///?}
    }

    public static boolean isModLoaded(String modId) {
        //? if fabric {
        return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded(modId);
        //?} else if neoforge {
        /*return net.neoforged.fml.loading.FMLLoader.getLoadingModList().getModFileById(modId) != null;
        *///?} else if forge {
        /*return net.minecraftforge.fml.ModList.get().isLoaded(modId);
        *///?}
    }

    public static ResourceLocation getResourceLocation(String namespace, String path) {
        //? if <= 1.20.1 {
        /*return ResourceLocation.tryBuild(namespace, path);
        *///?} else {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
        //?}
    }

    public static ResourceLocation getGuiLocation(String namespace, String path) {
        //? if <= 1.20.1 {
        /*return getResourceLocation(namespace, "textures/gui/sprites/" + path + ".png");
        *///?} else {
        return getResourceLocation(namespace, path);
        //?}
    }


    public static Component getCustomName(Object object) {
        try {
            //? if <= 1.21.1 {
            /*if (object instanceof ArmorStand armorStand) {
                return armorStand.getCustomName();
            }
            *///?} else {
            if (object instanceof net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandState) {
                return armorStandState.customName;
            }
            //?}
            else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean getNoBasePlate(Object object) {
        //? if <= 1.21.1 {
        /*if (object instanceof ArmorStand armorStand) {
            return armorStand.isNoBasePlate();
         }
         *///?} else {
        if (object instanceof net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandState) {
            return !armorStandState.showBasePlate;
        }
        //?}
        return false;
    }

    public static boolean getIsBaby(Object object) {
        //? if <= 1.21.1 {
        /*if (object instanceof ArmorStand armorStand) {
            return armorStand.isBaby();
        }
         *///?} else {
        if (object instanceof net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandState) {
            return armorStandState.isBaby;
        }
        //?}
        return false;
    }

    public static boolean getIsShowArms(Object object) {
        //? if <= 1.21.1 {
        /*if (object instanceof ArmorStand armorStand) {
            return armorStand.isShowArms();
        }
        *///?} else {
        if (object instanceof net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandState) {
            return armorStandState.showArms;
        }
        //?}
        return false;
    }

    public static boolean isInvisible(Object object) {
        //? if <= 1.21.1 {
        /*if (object instanceof ArmorStand armorStand) {
            return armorStand.isInvisible();
        }
        *///?} else {
        if (object instanceof net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandState) {
            return armorStandState.isInvisible;
        }
        //?}
        return false;
    }

    public static Rotations getHeadPose(Object object) {
        //? if <= 1.21.1 {
        /*if (object instanceof ArmorStand armorStand) {
            return armorStand.getHeadPose();
        }
        *///?} else {
        if (object instanceof net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandState) {
            return armorStandState.headPose;
        }
        //?}
        return new Rotations(0, 0, 0);
    }

    public static Rotations getBodyPose(Object object) {
        //? if <= 1.21.1 {
        /*if (object instanceof ArmorStand armorStand) {
            return armorStand.getBodyPose();
        }
        *///?} else {
        if (object instanceof net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandState) {
            return armorStandState.bodyPose;
        }
        //?}
        return new Rotations(0, 0, 0);
    }

    public static float getYRot(Object object) {
        //? if <= 1.21.1 {
        /*if (object instanceof ArmorStand armorStand) {
            return armorStand.getYRot();
        }
        *///?} else {
        if (object instanceof net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandState) {
            return armorStandState.yRot;
        }
        //?}
        return 0.0F;
    }

    public static int getPixel(NativeImage image, int x, int y) {
        //? if <= 1.21.1 {
        /*return image.getPixelRGBA(x, y);
        *///?} else {
        return image.getPixel(x, y);
        //?}
    }

    public static void setPixel(NativeImage image, int x, int y, int color) {
        //? if <= 1.21.1 {
        /*image.setPixelRGBA(x, y, color);
        *///?} else {
        image.setPixel(x, y, color);
        //?}
    }

    public static float getXRot(Rotations rot) {
        //? if <= 1.21.4 {
        return rot.getX();
        //?} else {
        /*return rot.x();
        *///?}
    }
    public static float getYRot(Rotations rot) {
        //? if <= 1.21.4 {
        return rot.getY();
        //?} else {
        /*return rot.y();
        *///?}
    }
    public static float getZRot(Rotations rot) {
        //? if <= 1.21.4 {
        return rot.getZ();
        //?} else {
        /*return rot.z();
        *///?}
    }


    //? if >= 1.21.4 {
    public static
        //? if >= 1.21.6 {
        /*com.mojang.blaze3d.pipeline.RenderPipeline
        *///?} else {
        Function<ResourceLocation, RenderType>
        //?}
        getGuiRender() {
            //? if >= 1.21.6 {
            /*return net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED;
            *///?} else {
            return RenderType::guiTextured;
            //?}
            }
    //?}

    public static int getARGBwhite(float alpha) {
        return (int) Math.floor(alpha * 255.0F) << 24 | 16777215;
    }


    public static void renderEntityInUi(GuiGraphics guiGraphics, LivingEntity entity, int x, int y, int width, int height, float scale,
                                        Vector3f translation, Quaternionf rotation,
                                        int leftOffset, int topOffset, int rightOffset, int bottomOffset) {
        //? if <= 1.21.5 {
        guiGraphics.enableScissor(x - leftOffset, y - topOffset, width - leftOffset - rightOffset, height - topOffset - bottomOffset);
        InventoryScreen.renderEntityInInventory(
                guiGraphics,
                x,
                y,
                scale,
                new Vector3f(0, 0, 0),
                rotation,
                null,
                entity
        );
        guiGraphics.disableScissor();
        //?} else {
        /*int left = x - leftOffset;
        int top = y - topOffset;
        int right = x - rightOffset;
        int bottom = y + bottomOffset;

        InventoryScreen.renderEntityInInventory(
                guiGraphics,
                left,
                top,
                right,
                bottom,
                scale,
                translation,
                rotation,
                null,
                entity
        );
        *///?}
    }


    public abstract static class VersionlessArmorStandCapeLayer extends RenderLayer<
            //? if <= 1.21.1 {
            /*ArmorStand,
            *///?} else {
            net.minecraft.client.renderer.entity.state.ArmorStandRenderState,
            //?}
            ArmorStandArmorModel>
    {
        public VersionlessArmorStandCapeLayer(RenderLayerParent<
                //? if <= 1.21.1 {
                /*ArmorStand,
                *///?} else {
                net.minecraft.client.renderer.entity.state.ArmorStandRenderState,
                //?}
                ArmorStandArmorModel> renderer) {
            super(renderer);
        }
    }

    public interface VersionlessArmorStandCape extends RenderLayerParent <
            //? if <= 1.21.1 {
            /*ArmorStand,
            *///?} else {
            net.minecraft.client.renderer.entity.state.ArmorStandRenderState,
            //?}
            ArmorStandArmorModel> {}

}
