package com.danrus.pas.impl.data.skin;

import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.DataType;
import com.danrus.pas.impl.data.common.AbstractClientLevelDataProvider;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.mc.ModUtils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class ClientLevelSkinData extends AbstractClientLevelDataProvider<SkinData> {

    @Override
    protected @Nullable Identifier getTexture(AbstractClientPlayer player) {
        return player.getSkin().body().texturePath();
    }

    @Override
    protected SkinData createDataHolder() {
        return new SkinData();
    }

    @Override
    protected DataRepository<SkinData> getDataManager() {
        return PasManager.getInstance().getSkinDataManager();
    }

    @Override
    public DataType getDataType() {
        return DataType.SKIN;
    }
}
