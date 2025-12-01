package com.danrus.pas.impl.data.skin;

import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.DataStoreKey;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.data.common.AbstractClientLevelDataProvider;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.ModUtils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ClientLevelSkinData extends AbstractClientLevelDataProvider<SkinData> {

    @Override
    protected @Nullable ResourceLocation getTexture(AbstractClientPlayer player) {
        return ModUtils.getPlayerSkinTexture(player);
    }

    @Override
    protected SkinData createDataHolder(NameInfo info) {
        return new SkinData(info);
    }

    @Override
    protected DataRepository<SkinData> getDataManager() {
        return PasManager.getInstance().getSkinDataManager();
    }

    @Override
    protected DataStoreKey getKey(NameInfo info) {
        return DataStoreKey.forSkin(info);
    }

    @Override
    public DataStoreKey.DataType getDataType() {
        return DataStoreKey.DataType.SKIN;
    }
}
