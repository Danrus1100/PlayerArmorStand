package com.danrus.pas.api;

import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.TextureProvidersManager;
import com.danrus.pas.managers.PasManager;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * PlayerArmorStandsApi provides access to the data manager and skin provider manager.
 * It allows retrieval of skin data by player name.
 * !!!WARNING!!!: in 0.X.X versions, this API is unstable and may change in future releases and betas.
 */

public class PlayerArmorStandsApi {

    public DataRepository getDataManager() {
        return PasManager.getInstance().getSkinDataManager();
    }

    public TextureProvidersManager getSkinProviderManager() {
        return PasManager.getInstance().getSkinProviderManager();
    }
}
