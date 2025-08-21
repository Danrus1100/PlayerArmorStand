package com.danrus.pas.render.bb;

import com.danrus.pas.render.PlayerArmorStandModel;
import com.danrus.pas.render.bb.core.BbConverter;
import com.danrus.pas.render.bb.core.BbModelContainer;
import net.minecraft.client.model.geom.ModelPart;

import java.util.List;

public class BbPasModel extends PlayerArmorStandModel {

    BbModelContainer container;

    public BbPasModel(BbModelContainer container) {
        this(BbConverter.convertToLayerDefinition(container).bakeRoot());
        this.container = container;
    }

    public BbPasModel(ModelPart modelPart) {
        super(modelPart);
    }

    public List<ModelPart> getBaseParts() {
        return List.of(
                head, hat,
                body, jacket,
                leftArm, leftSleeve,
                rightArm, rightSleeve,
                leftLeg, leftPants,
                rightLeg, rightPants,
                rightSlimArm, rightSlimSleeve,
                leftSlimArm, leftSlimSleeve
        );
    }

    public List<String> getPartNames() {
        return List.of(
                "head", "hat",
                "body", "jacket",
                "leftArm", "leftSleeve",
                "rightArm", "rightSleeve",
                "leftLeg", "leftPants",
                "rightLeg", "rightPants",
                "rightSlimArm", "rightSlimSleeve",
                "leftSlimArm", "leftSlimSleeve"
        );
    }

    public BbModelContainer getContainer() {
        return container;
    }
}
