package com.danrus.pas.render.item;

import com.danrus.pas.api.info.NameInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Rotations;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class PasItemModelPart {
    public static Codec<PasItemModelPart> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.VECTOR3F.optionalFieldOf("rotation", new Vector3f()).forGetter(part -> part.rotation),
            Codec.STRING.optionalFieldOf("mode", "original").forGetter(part -> part.mode.name())
    ).apply(instance, (rotation, mode) -> {
        return new PasItemModelPart(rotation, Mode.valueOf(mode.toUpperCase()));
    }));

    public
    //? if <1.21.11
    Vector3f
    //? if >=1.21.11
    /*Vector3fc*/
            rotation;
    public Mode mode;

    public PasItemModelPart(
            //? if <1.21.11
            Vector3f
            //? if >=1.21.11
            /*Vector3fc*/
                    rotation, Mode mode) {
        this.rotation = rotation;
        this.mode = mode;
    }

    public PasItemModelPart(
            //? if <1.21.11
            Vector3f
            //? if >=1.21.11
            /*Vector3fc*/
                    rotation) {
        this(rotation, Mode.ORIGINAL);
    }

    public Rotations toRotations() {
        return new Rotations(
                this.rotation.x(),
                this.rotation.y(),
                this.rotation.z()
        );
    }

    public enum Mode {
        ORIGINAL,
        INVISIBLE,
        DYNAMIC,
        PLAYER;


        public boolean showPlayerPart(NameInfo info) {
            return this == PLAYER || this == DYNAMIC && !info.isEmpty();
        }

        public boolean showOriginalPart(NameInfo info) {
            return this == ORIGINAL || this == DYNAMIC && info.isEmpty();
        }
    }
}
