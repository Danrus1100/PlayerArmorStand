package com.danrus.pas.impl.data.skin;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.DataStoreKey;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.data.common.AbstractFileTextureDataProvider;
import com.danrus.pas.impl.holder.SkinData;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.ModUtils;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;

public class FileTextureSkinData extends AbstractFileTextureDataProvider<SkinData> {

    public static final Path SKINS_PATH = ModUtils.getGameDir().resolve("pas/skins");

    @Override
    protected Path getFilePath(NameInfo info) {
        return SKINS_PATH.resolve(info.base() + ".png");
    }

    @Override
    protected Path getCachePath() {
        return SKINS_PATH;
    }

    @Override
    protected SkinData createDataHolder(NameInfo info, ResourceLocation texture) {
        SkinData newData = new SkinData(info);
        newData.setTexture(texture);
        return newData;
    }

    @Override
    protected DataRepository<SkinData> getDataManager() {
        return PasManager.getInstance().getSkinDataManager();
    }

    @Override
    protected String getProviderCode() {
        return "F";
    }

    @Override
    protected Class<? extends DataHolder> getDataHolderClass() {
        return SkinData.class;
    }

    @Override
    protected DataStoreKey getKey(NameInfo info) {
        return DataStoreKey.forSkin(info);
    }

    @Override
    public String getName() {
        return "texture_file";
    }

    @Override
    public DataStoreKey.DataType getDataType() {
        return DataStoreKey.DataType.SKIN;
    }
}
