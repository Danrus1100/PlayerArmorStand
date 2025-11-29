package com.danrus.pas;

import com.danrus.pas.api.reg.FeatureRegistry;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.impl.features.*;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.impl.translators.cape.MinecraftCapesTranslator;
import com.danrus.pas.impl.translators.cape.MojangCapeTranslator;
import com.danrus.pas.impl.translators.cape.NamemcCapeTranslator;
import com.danrus.pas.impl.translators.skin.FileSkinTranslator;
import com.danrus.pas.impl.translators.skin.MojangSkinTranslator;
import com.danrus.pas.impl.translators.skin.NamemcSkinTranslator;
import com.danrus.pas.managers.PasManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerArmorStandsClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("PlayerArmorStands");

    //? >=1.21.9
    public static net.minecraft.client.model.geom.ModelPart capeDef = com.danrus.pas.render.armorstand.PasCapeModel.createCapeLayer().bakeRoot();

    public static void initialize() {
        PasConfig.init();
        PasManager.getInstance();
        FeatureRegistry features = FeatureRegistry.getInstance();
        features.register(OverlayFeature.class);
        features.register(CapeFeature.class);
        features.register(SkinProviderFeature.class);
        features.register(SlimFeature.class);
        features.register(DisplayNameFeature.class);

        InfoTranslators translator = InfoTranslators.getInstance();
        translator.register(SkinData.class, new NamemcSkinTranslator());
        translator.register(SkinData.class, new MojangSkinTranslator());
        translator.register(SkinData.class, new FileSkinTranslator());
        translator.register(CapeData.class, new NamemcCapeTranslator());
        translator.register(CapeData.class, new MojangCapeTranslator());
        translator.register(CapeData.class, new MinecraftCapesTranslator());
    }
}
