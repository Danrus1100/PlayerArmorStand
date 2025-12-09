package com.danrus.pas.render.item;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.render.armorstand.PlayerArmorStandModel;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Rotations;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class PasItemModelPart {
    public static Codec<PasItemModelPart> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.VECTOR3F.optionalFieldOf("rotation", new Vector3f()).forGetter(part -> part.rotation),
            Codec.STRING.optionalFieldOf("mode", "original").forGetter(part -> part.mode.name())
    ).apply(instance, (rotation, mode) -> {
        return new PasItemModelPart(rotation, Mode.valueOf(mode.toUpperCase()));
    }));

    public Vector3f rotation;
    public Mode mode;

    public PasItemModelPart(Vector3f rotation, Mode mode) {
        this.rotation = rotation;
        this.mode = mode;
    }

    public PasItemModelPart(Vector3f rotation) {
        this(rotation, Mode.ORIGINAL);
    }

    public Rotations toRotations() {
        return new Rotations(
                this.rotation.x,
                this.rotation.y,
                this.rotation.z
        );
    }

    public enum Mode {
        ORIGINAL,
        INVISIBLE,
        DYNAMIC,
        PLAYER;


        public boolean showPlayerPart(@Nullable DataHolder data) {
            if (data == null) return false;
            return this == PLAYER || this == DYNAMIC && !data.getInfo().isEmpty() && !PlayerArmorStandModel.showArmorStandWhileDownload(data);
        }

        public boolean showOriginalPart(@Nullable DataHolder data) {
            if (data == null) return true;
            return this == ORIGINAL || this == DYNAMIC && data.getInfo().isEmpty() && PlayerArmorStandModel.showArmorStandWhileDownload(data);
        }
    }
}
