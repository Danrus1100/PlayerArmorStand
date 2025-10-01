package com.danrus.pas;

import com.danrus.pas.render.PasCapeModel;
import net.minecraft.client.model.PlayerCapeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerArmorStandsClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("PlayerArmorStands");

    public static ModelPart capeDef;

    public static void initialize() {
        capeDef = PasCapeModel.createCapeLayer().bakeRoot();
    }
}
