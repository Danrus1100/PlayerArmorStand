package com.danrus.pas.impl.data.common;

import com.danrus.pas.api.*;
import com.danrus.pas.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

    public abstract class AbstractClientLevelDataProvider<T extends DataHolder> implements DataProvider<T> {

    Minecraft mc = Minecraft.getInstance();

    @Override
    public T get(NameInfo info) {
        if (!ModConfig.get().tryApplyFromServerPlayer) {
            return null;
        }
        AtomicReference<T> dataAtomicReference = new AtomicReference<>(createDataHolder(info));
        if (mc.level != null) {
            mc.level.players().stream()
                    .filter(player -> player.getName().getString().equals(info.base()))
                    .findFirst()
                    .ifPresent(
                            player -> {
                                if (getTexture(player) != null) {
                                    dataAtomicReference.get().setStatus(DownloadStatus.COMPLETED);
                                    dataAtomicReference.get().setTexture(getTexture(player));
                                }
                            }
                    );
        }



        if (dataAtomicReference.get().getStatus() == DownloadStatus.COMPLETED) {
            getDataManager().store(info, dataAtomicReference.get());
            return dataAtomicReference.get();
        }

        return null;
    }

    @Override
    public boolean delete(NameInfo info) {
        return false;
    }

    @Override
    public HashMap<String, T> getAll() {
        if (mc.level == null) {
            return new HashMap<>();
        }
        return mc.level.players().stream()
                .map(player -> {
                    T data = createDataHolder(NameInfo.parse(player.getName().getString()));
                    if (getTexture(player) != null) {
                        data.setStatus(DownloadStatus.COMPLETED);
                        data.setTexture(getTexture(player));
                    }
                    return data;
                })
                .collect(
                        HashMap::new,
                        (map, data) -> map.put(data.getInfo().base(), data),
                        HashMap::putAll
                );
    }

    @Override
    public void store(NameInfo info, T data) {

    }

    @Override
    public void invalidateData(NameInfo info) {

    }

    @Override
    public String getName() {
        return "level";
    }

    @Nullable
    protected abstract ResourceLocation getTexture(AbstractClientPlayer player);
    protected abstract T createDataHolder(NameInfo info);
    protected abstract DataRepository<T> getDataManager();
}
