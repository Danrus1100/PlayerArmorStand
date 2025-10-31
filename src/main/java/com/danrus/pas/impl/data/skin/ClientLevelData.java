package com.danrus.pas.impl.data.skin;

import com.danrus.pas.api.DataProvider;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.LegacySkinData;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.managers.PasManager;
import com.danrus.pas.utils.VersioningUtils;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ClientLevelData implements DataProvider<LegacySkinData> {

    Minecraft mc = Minecraft.getInstance();

    @Override
    public LegacySkinData get(NameInfo info) {

        if (!ModConfig.get().tryApplyFromServerPlayer) {
            return null;
        }
        AtomicReference<LegacySkinData> dataAtomicReference = new AtomicReference<>(new LegacySkinData(info));
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
            PasManager.getInstance().getDataManager().store(info, dataAtomicReference.get());
            return dataAtomicReference.get();
        }

        return null;
    }

    @Override
    public boolean delete(NameInfo info) {
        return false;
    }

    @Override
    public HashMap<String, LegacySkinData> getAll() {
        if (mc.level == null) {
            return new HashMap<>();
        }
        return mc.level.players().stream()
                .map(player -> {
                    LegacySkinData data = new LegacySkinData(player.getName().getString());
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
        return data instanceof LegacySkinData;
    }

    @Override
    public String getName() {
        return "level";
    }
}
