package com.danrus.pas.impl.data;

import com.danrus.pas.api.DataHolder;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.utils.VersioningUtils;
import com.danrus.pas.managers.SkinManager;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ClientLevelData implements DataHolder<SkinData> {

    Minecraft mc = Minecraft.getInstance();

    @Override
    public SkinData get(NameInfo info) {

        if (!ModConfig.get().tryApplyFromServerPlayer) {
            return null;
        }

        AtomicReference<SkinData> dataAtomicReference = new AtomicReference<>(new SkinData(info));
        if (mc.level != null) {
            mc.level.players().stream()
                    .filter(player -> player.getName().getString().equals(info.base()))
                    .findFirst()
                    .ifPresent(
                            player -> {
                                dataAtomicReference.get().setStatus(DownloadStatus.COMPLETED);
                                dataAtomicReference.get().setSkinTexture(VersioningUtils.getPlayerSkinTexture(player));
                                if (VersioningUtils.getPlayerCapeTexture(player) != null) {
                                    dataAtomicReference.get().setCapeTexture(VersioningUtils.getPlayerCapeTexture(player));
                                }
                            }
                    );
        }

        if (dataAtomicReference.get().getStatus() == DownloadStatus.COMPLETED) {
            SkinManager.getInstance().getDataManager().store(info, dataAtomicReference.get());
            return dataAtomicReference.get();
        }

        return null;
    }

    @Override
    public boolean delete(NameInfo info) {
        return false;
    }

    @Override
    public HashMap<String, SkinData> getAll() {
        if (mc.level == null) {
            return new HashMap<>();
        }
        return mc.level.players().stream()
                .map(player -> {
                    SkinData data = new SkinData(player.getName().getString());
                    data.setStatus(DownloadStatus.COMPLETED);
                    data.setSkinTexture(VersioningUtils.getPlayerSkinTexture(player));
                    if (VersioningUtils.getPlayerCapeTexture(player) != null) {
                        data.setCapeTexture(VersioningUtils.getPlayerCapeTexture(player));
                    }
                    return data;
                })
                .collect(
                        HashMap::new,
                        (map, data) -> map.put(data.getName(), data),
                        HashMap::putAll
                );
    }

    @Override
    public void store(NameInfo info, Object data) {
        // NO-OP
    }

    @Override
    public void invalidateData(NameInfo info) {
        // NO-OP
    }

    @Override
    public boolean isCompatibleWith(Object data) {
        return data instanceof SkinData;
    }

    @Override
    public String getName() {
        return "level";
    }
}
