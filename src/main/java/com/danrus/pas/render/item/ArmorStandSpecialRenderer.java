package com.danrus.pas.render.item;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.danrus.pas.utils.Rl;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.SkullSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ArmorStandSpecialRenderer implements SpecialModelRenderer<NameInfo> {

    private final PlayerArmorStandModel model;
    private final ResourceLocation baseTexture;
    private final ResourceLocation defaultSkin;

    public ArmorStandSpecialRenderer(PlayerArmorStandModel model, ResourceLocation baseTexture, ResourceLocation defaultSkin) {
        this.model = model;
        this.baseTexture = baseTexture;
        this.defaultSkin = defaultSkin;
    }

    @Override
    public void render(@Nullable NameInfo info, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        NameInfo currentInfo = info != null ? info : new NameInfo();
//        RenderType skin = RenderType.entityTranslucent(currentInfo.isEmpty() ? defaultSkin : PasManager.getInstance().getSkinWithOverlayTexture(info));
        RenderType base = RenderType.entityCutout(baseTexture);

        poseStack.translate(0.5, 0.75, 0.5);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.scale(0.5f, 0.5f, 0.5f);

        for (ModelPart part : this.model.getBasePartsForItem()) {
            VertexConsumer skinConsumer = bufferSource.getBuffer(base);
            part.visible = true;
            part.render(poseStack, skinConsumer, packedLight, packedOverlay);
        }

        RenderType head;
        Iterable<ModelPart> headParts;
        if (currentInfo.isEmpty()) {
            head = base;
            headParts = this.model.getOriginalHead();
        } else {
            head = RenderType.entityTranslucent(currentInfo.isEmpty() ? defaultSkin : PasManager.getInstance().getSkinWithOverlayTexture(info));
            headParts = this.model.headParts();
        }
        for (ModelPart part : headParts) {
            VertexConsumer skinConsumer = bufferSource.getBuffer(head);
            part.render(poseStack, skinConsumer, packedLight, packedOverlay);
        }
    }

    @Override
    public @Nullable NameInfo extractArgument(ItemStack stack) {
        return NameInfo.parse(stack.getCustomName());
    }

    public static record Unbaked(Optional<ResourceLocation> base, Optional<ResourceLocation> defaultSkin) implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        ResourceLocation.CODEC.optionalFieldOf("base").forGetter(ArmorStandSpecialRenderer.Unbaked::base),
                        ResourceLocation.CODEC.optionalFieldOf("default_skin").forGetter(ArmorStandSpecialRenderer.Unbaked::defaultSkin)
                ).apply(instance, ArmorStandSpecialRenderer.Unbaked::new)
        );

        @Override
        public @Nullable SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            PlayerArmorStandModel pasModel = new PlayerArmorStandModel(modelSet.bakeLayer(ModelLayers.ARMOR_STAND));
            ResourceLocation baseTexture = this.base.orElse(Rl.vanilla("textures/entity/armorstand/wood.png"));
            ResourceLocation defaultSkin = this.defaultSkin.orElse(Rl.vanilla("textures/entity/player/wide/steve.png"));
            return new ArmorStandSpecialRenderer(pasModel, baseTexture, defaultSkin);
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
