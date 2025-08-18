package com.danrus.pas.api.event;

import com.danrus.pas.core.event.PasEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PasPreRenderEvent extends PasEvent {
    private final ArmorStand armorStand;
    private final String customName;
    private final PoseStack poseStack;
    private final MultiBufferSource multiBufferSource;
    private final Supplier<EntityModel> modelGetter;
    private final Consumer<EntityModel> modelSetter;

    public PasPreRenderEvent(ArmorStand armorStand, String customName, PoseStack poseStack, MultiBufferSource multiBufferSource, Supplier<EntityModel> modelGetter, Consumer<EntityModel> modelSetter) {
        this.armorStand = armorStand;
        this.customName = customName;
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
        this.modelGetter = modelGetter;
        this.modelSetter = modelSetter;
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

    public void setModel(EntityModel model) {
        modelSetter.accept(model);
    }
}
