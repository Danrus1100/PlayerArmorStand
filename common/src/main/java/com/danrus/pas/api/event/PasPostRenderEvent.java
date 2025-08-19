package com.danrus.pas.api.event;

import com.danrus.pas.core.event.PasEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.util.function.Supplier;

public class PasPostRenderEvent extends PasEvent {

    private final ArmorStand armorStand;
    private final String customName;
    private final PoseStack poseStack;
    private final MultiBufferSource multiBufferSource;
    private final Supplier<EntityModel> modelGetter;
    public PasPostRenderEvent(ArmorStand armorStand, String customName, PoseStack poseStack, MultiBufferSource multiBufferSource, Supplier<EntityModel> modelGetter) {
        this.armorStand = armorStand;
        this.customName = customName;
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
        this.modelGetter = modelGetter;
    }

    public ArmorStand armorStand() {
        return armorStand;
    }

    public String hasCustomName() {
        return customName;
    }

    public PoseStack poseStack() {
        return poseStack;
    }

    public MultiBufferSource multiBufferSource() {
        return multiBufferSource;
    }


    public EntityModel model() {
        return modelGetter.get();
    }

}
