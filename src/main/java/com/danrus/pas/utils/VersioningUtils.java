package com.danrus.pas.utils;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.render.armorstand.RenderVersionContext;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;

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

    public static Component getCustomName(Object object) {
        try {
            //? if <= 1.21.1 {
            /*if (object instanceof ArmorStand armorStand) {
                return armorStand.getCustomName();
            }
            *///?} else {
            if (object instanceof net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStandState) {
                //? <1.21.9 {
                return armorStandState.customName;
                //?} else {
                /*return ((com.danrus.pas.extenders.ArmorStandRenderStateExtender) armorStandState).pas$getCustomName();
                *///?}
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

    public static ResourceLocation getPlayerSkinTexture(AbstractClientPlayer player){
        //? if <= 1.20.1 {
        /*return player.getSkinTextureLocation();
        *///?} else if >1.20.1 && <1.21.9 {
        return player.getSkin().texture();
        //?} else {
        /*return player.getSkin().body().texturePath();
        *///?}
    }

    public static ResourceLocation getPlayerCapeTexture(AbstractClientPlayer player){
        //? if <= 1.20.1 {
        /*return player.getCloakTextureLocation();
        *///?} else if >1.20.1 && <1.21.9 {
        return player.getSkin().capeTexture();
        //?} else {
        /*try {
            return player.getSkin().cape().texturePath();
        } catch (Exception e) {
            return CapeData.DEFAULT_TEXTURE;
        }
        *///?}
    }

    public static void copyPartPose(ModelPart from, ModelPart to){
        //? <1.21.9 {
        to.copyFrom(from);
        //?} else {
        /*to.x = from.x;
        to.y = from.y;
        to.z = from.z;
        to.xRot = from.xRot;
        to.yRot = from.yRot;
        to.zRot = from.zRot;
        to.xScale = from.xScale;
        to.yScale = from.yScale;
        to.zScale = from.zScale;
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
        //? >= 1.21.9
        /*private final com.danrus.pas.render.armorstand.PasCapeModel capeModel;*/

        public VersionlessArmorStandCapeLayer(RenderLayerParent<
                //? if <= 1.21.1 {
                /*ArmorStand,
                *///?} else {
                net.minecraft.client.renderer.entity.state.ArmorStandRenderState,
                //?}
                ArmorStandArmorModel> renderer) {
            super(renderer);
            //? >= 1.21.9
            /*capeModel = new com.danrus.pas.render.armorstand.PasCapeModel();*/
        }

        //? <1.21.9 {
        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
                           //? if <= 1.21.1 {
                /*ArmorStand
                 *///?} else {
                           net.minecraft.client.renderer.entity.state.ArmorStandRenderState
                                   //?}
                                   armorStand,
                           float f1, float f2
                           //? if <= 1.21.1
                /*, float f3, float f4, float f5, float f6*/
        )
        //?} else {
        /*@Override
        public void submit(PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector collector, int i, net.minecraft.client.renderer.entity.state.ArmorStandRenderState armorStand, float f, float g)
        *///?}
        {
            if (!ModConfig.get().enableMod || VersioningUtils.isInvisible(armorStand)) {
                return;
            }

            Component customName = VersioningUtils.getCustomName(armorStand);
            boolean isBaby = VersioningUtils.getIsBaby(armorStand);

            if (customName == null) {
                return;
            }

            NameInfo info = NameInfo.parse(customName);

            if (!info.wantCape()) {
                return;
            }

            if (!(this.getParentModel() instanceof PlayerArmorStandModel model)) {
                return;
            }

            RenderVersionContext context = new RenderVersionContext(
                    //? <1.21.9
                    model
                    //? >=1.21.9
                    /*capeModel*/
            );

            //? <1.21.9 {
            context.putData(multiBufferSource);
            //?} else {
            /*context.putData(capeModel);
            context.putData(armorStand);
            context.putData(collector);
             *///?}
            draw(poseStack, context, info, i, isBaby);
        }

        protected abstract void draw(PoseStack poseStack, RenderVersionContext context, NameInfo info, int light, boolean isBaby);
    }

    public interface VersionlessArmorStandCape extends RenderLayerParent <
            //? if <= 1.21.1 {
            /*ArmorStand,
            *///?} else {
            net.minecraft.client.renderer.entity.state.ArmorStandRenderState,
            //?}
            ArmorStandArmorModel> {}

}
