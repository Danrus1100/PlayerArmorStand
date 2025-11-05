package com.danrus.pas.impl.translators.common;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.InfoTranslator;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.features.SkinProviderFeature;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.StringUtils;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractSimpleTranslator implements InfoTranslator {

    @Override
    public boolean isApplicable(NameInfo info) {
        try {
            SkinProviderFeature feature = info.getFeature(SkinProviderFeature.class);

            if (feature == null) {
                PlayerArmorStandsClient.LOGGER.error("SkinProviderFeature is NULL for {}", info);
                return false;
            }

            String provider = feature.getProvider();

            if (provider == null) {
                PlayerArmorStandsClient.LOGGER.error("Provider is NULL for {}", info);
                return false;
            }

            boolean result = getLiteral().equals(provider);

            PlayerArmorStandsClient.LOGGER.info("isApplicable: {} - provider='{}' literal='{}' = {}",
                    this.getClass().getSimpleName(), provider, getLiteral(), result);

            return result;

        } catch (Exception e) {
            PlayerArmorStandsClient.LOGGER.error("Error checking applicability for {}", info, e);
            return false;
        }
    }


    @Override
    public ResourceLocation toResourceLocation(NameInfo info) {
        return Rl.pas(getPrefix() + "/" + (shouldEncode() ? StringUtils.encodeToSha256(info.base()) : info.base()));
    }

    @Override
    public String toFileName(NameInfo info) {
        String name = shouldEncode() ? StringUtils.encodeToSha256(info.base()) : info.base();
        String suffix = getSuffix().isEmpty() ? "" : "_" + getSuffix();
        return name + suffix;
    }

    abstract String getLiteral();
    protected abstract String getPrefix();
    protected abstract String getSuffix();
    abstract boolean shouldEncode();
}
