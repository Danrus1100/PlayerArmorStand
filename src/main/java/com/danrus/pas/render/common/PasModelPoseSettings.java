package com.danrus.pas.render.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import org.joml.Vector3f;

public class PasModelPoseSettings {
    public static PasModelPartSettings DEFAULT_HEAD = new PasModelPartSettings(new Vector3f(0, 0, 0), PasModelPartSettings.Mode.DYNAMIC);
    public static PasModelPartSettings DEFAULT_BODY = new PasModelPartSettings(new Vector3f(0, 0, 0));
    public static PasModelPartSettings DEFAULT_LEFT_LEG = new PasModelPartSettings(new Vector3f(-1, 0, -1));
    public static PasModelPartSettings DEFAULT_RIGHT_LEG = new PasModelPartSettings(new Vector3f(1, 0, 1));
    public static PasModelPartSettings DEFAULT_LEFT_ARM = new PasModelPartSettings(new Vector3f(-10, 0, -10), PasModelPartSettings.Mode.INVISIBLE);
    public static PasModelPartSettings DEFAULT_RIGHT_ARM = new PasModelPartSettings(new Vector3f(-15, 0, 10), PasModelPartSettings.Mode.INVISIBLE);

    public static Codec<PasModelPoseSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PasModelPartSettings.CODEC.optionalFieldOf("head", DEFAULT_HEAD).forGetter(state -> state.head),
            PasModelPartSettings.CODEC.optionalFieldOf("body", DEFAULT_BODY).forGetter(state -> state.body),
            PasModelPartSettings.CODEC.optionalFieldOf("left_leg", DEFAULT_LEFT_LEG).forGetter(state -> state.leftLeg),
            PasModelPartSettings.CODEC.optionalFieldOf("right_leg", DEFAULT_RIGHT_LEG).forGetter(state -> state.rightLeg),
            PasModelPartSettings.CODEC.optionalFieldOf("left_arm", DEFAULT_LEFT_ARM).forGetter(state -> state.leftArm),
            PasModelPartSettings.CODEC.optionalFieldOf("right_arm", DEFAULT_RIGHT_ARM).forGetter(state -> state.rightArm),
            Codec.BOOL.optionalFieldOf("baseplate", true).forGetter(state -> state.baseplate)
    ).apply(instance, PasModelPoseSettings::new));

    public PasModelPartSettings head;
    public PasModelPartSettings body;
    public PasModelPartSettings leftLeg;
    public PasModelPartSettings rightLeg;
    public PasModelPartSettings leftArm;
    public PasModelPartSettings rightArm;
    public boolean baseplate;


    public PasModelPoseSettings() {
        this.head = DEFAULT_HEAD;
        this.body = DEFAULT_BODY;
        this.leftLeg = DEFAULT_LEFT_LEG;
        this.rightLeg = DEFAULT_RIGHT_LEG;
        this.leftArm = DEFAULT_LEFT_ARM;
        this.rightArm = DEFAULT_RIGHT_ARM;
        this.baseplate = true;
    }

    public PasModelPoseSettings(
            PasModelPartSettings head,
            PasModelPartSettings body,
            PasModelPartSettings leftLeg,
            PasModelPartSettings rightLeg,
            PasModelPartSettings leftArm,
            PasModelPartSettings rightArm,
            boolean baseplate
    ) {
        this.head = head;
        this.body = body;
        this.leftLeg = leftLeg;
        this.rightLeg = rightLeg;
        this.leftArm = leftArm;
        this.rightArm = rightArm;
        this.baseplate = baseplate;
    }

    public ArmorStandRenderState toRenderState() {
        ArmorStandRenderState state = new ArmorStandRenderState();
        state.leftArmPose = this.leftArm.toRotations();
        state.rightArmPose = this.rightArm.toRotations();
        state.leftLegPose = this.leftLeg.toRotations();
        state.rightLegPose = this.rightLeg.toRotations();
        state.bodyPose = this.body.toRotations();
        state.headPose = this.head.toRotations();
        state.showBasePlate = this.baseplate;
        return state;
    }
}