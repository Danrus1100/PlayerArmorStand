package com.danrus.pas.impl.data.common;

import com.danrus.pas.api.*;
import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataProvider;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoLike;
import com.danrus.pas.api.info.NameInfoPattern;
import com.danrus.pas.config.PasConfig;
import com.danrus.pas.utils.info.NameInfoMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractClientLevelDataProvider<T extends DataHolder> implements DataProvider<T> {

    @Override
    public Optional<T> get(NameInfo info) {
        if (Minecraft.getInstance().level == null) {
            return Optional.empty();
        }
        if (!PasConfig.getInstance().isTryApplyFromServerPlayer()) {
            return Optional.empty();
        }
        T holder = createDataHolder();
        if (Minecraft.getInstance().level != null) {
            Minecraft.getInstance().level.players().stream()
                    .filter(player -> player.getName().getString().equals(info.base()))
                    .findFirst()
                    .ifPresent(
                            player -> {
                                if (getTexture(player) != null) {
                                    holder.setStatus(DownloadStatus.COMPLETED);
                                    holder.setTexture(getTexture(player));
                                }
                            }
                    );
        }

        if (holder.getStatus() == DownloadStatus.COMPLETED) {
            getDataManager().store(info, holder);
            return Optional.of(holder);
        }

        return Optional.empty();
    }

    @Override
    public Optional<T> findFirst(NameInfoLike infoLike) {
        return switch (infoLike) {
            case NameInfo info -> get(info);
            case NameInfoPattern pattern -> getAll().findFirst(pattern).map(Map.Entry::getValue);
        };
    }

    @Override
    public Collection<T> findAll(NameInfoLike infoLike) {
        return getAll().findAll(infoLike).values();
    }

    @Override
    public boolean deleteAllOf(NameInfoLike info) {
        return false;
    }

    @Override
    public NameInfoMap<T> getAll() {
        if (Minecraft.getInstance().level == null) {
            return new NameInfoMap<>();
        }
        NameInfoMap<T> result = new NameInfoMap<>();
        Minecraft.getInstance().level.players().forEach(player -> {
            T data = createDataHolder();
            ResourceLocation texture = getTexture(player);
            if (texture != null) {
                data.setStatus(DownloadStatus.COMPLETED);
                data.setTexture(texture);
            }
            result.put(new NameInfo(player.getName().getString()), data);
        });
        return result;
    }

    @Override
    public void store(NameInfo info, T data) {
    }

    @Override
    public void invalidateData(NameInfoLike info) {
    }

    @Override
    public String getName() {
        return "level";
    }

    @Nullable
    protected abstract ResourceLocation getTexture(AbstractClientPlayer player);
    protected abstract T createDataHolder();
    protected abstract DataRepository<T> getDataManager();
}
