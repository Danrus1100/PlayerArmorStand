package com.danrus.pas.utils.data;

import com.danrus.pas.api.DataCache;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.SkinData;
import com.danrus.pas.config.ModConfig;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.managers.SkinManger;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ClientLevelCache implements DataCache<SkinData> {

    Minecraft mc = Minecraft.getInstance();

    @Override
    public SkinData get(String string) {

        if (!ModConfig.get().tryApplyFromServerPlayer) {
            return null;
        }

        List<String> matches = StringUtils.matchASName(string);
        String name = matches.get(0);
        String params = matches.get(1).toUpperCase();

        AtomicReference<SkinData> dataAtomicReference = new AtomicReference<>(new SkinData(name, params));
        if (mc.level != null) {
            mc.level.players().stream()
                    .filter(player -> player.getName().getString().equals(name))
                    .findFirst()
                    .ifPresent(
                            player -> {
                                dataAtomicReference.get().setStatus(DownloadStatus.COMPLETED);
                                dataAtomicReference.get().setSkinTexture(player.getSkin().texture());
                                if (player.getSkin().capeTexture() != null) {
                                    dataAtomicReference.get().setCapeTexture(player.getSkin().capeTexture());
                                }
                            }
                    );
        }

        if (dataAtomicReference.get().getStatus() == DownloadStatus.COMPLETED) {
            SkinManger.getInstance().getDataManager().store(name, dataAtomicReference.get());
            return dataAtomicReference.get();
        }

        return null;
    }

    @Override
    public boolean delete(String string) {
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
                    data.setSkinTexture(player.getSkin().texture());
                    if (player.getSkin().capeTexture() != null) {
                        data.setCapeTexture(player.getSkin().capeTexture());
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
    public void store(String name, Object data) {
        // NO-OP
    }

    @Override
    public void invalidateData(String name) {
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
