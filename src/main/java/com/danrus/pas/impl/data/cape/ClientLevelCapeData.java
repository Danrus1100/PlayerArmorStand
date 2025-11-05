package com.danrus.pas.impl.data.cape;

import com.danrus.pas.api.DataRepository;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.impl.data.common.AbstractClientLevelDataProvider;
import com.danrus.pas.impl.holder.CapeData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ClientLevelCapeData extends AbstractClientLevelDataProvider<CapeData> {

    @Override
    protected @Nullable ResourceLocation getTexture(AbstractClientPlayer player) {
        return VersioningUtils.getPlayerCapeTexture(player);
    }

    @Override
    protected CapeData createDataHolder(NameInfo info) {
        return new CapeData(info);
    }

    @Override
    protected DataRepository<CapeData> getDataManager() {
        return PasManager.getInstance().getCapeDataManager();
    }
}
