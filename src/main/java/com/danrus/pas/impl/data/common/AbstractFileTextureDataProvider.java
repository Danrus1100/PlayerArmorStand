package com.danrus.pas.impl.data.common;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.DataHolder;
import com.danrus.pas.api.DataProvider;
import com.danrus.pas.api.DataRepository;
import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.utils.Rl;
import com.danrus.pas.utils.StringUtils;
import com.danrus.pas.utils.TextureUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractFileTextureDataProvider<T extends DataHolder> implements DataProvider<T> {

    private final Map<String, T> cache = new HashMap<>();

    @Override
    public T get(NameInfo info) {
        if (cache.containsKey(info.base())) {
            return cache.get(info.base());
        }

        if (!info.getDesiredProvider().equals(getProviderCode())) {
            return null;
        }

        String encodedName = StringUtils.encodeToSha256(info.base());
        if (!isValidName(info.base())) {
            return null;
        }

        Path filePath = getFilePath(info);
        ResourceLocation textureLocation = InfoTranslators.getInstance().toResourceLocation(getDataHolderClass(), info);

        if (filePath.toFile().exists()) {
            Minecraft.getInstance().execute(() -> {
                TextureUtils.registerTexture(filePath, textureLocation, true);
                getDataManager().store(info, createDataHolder(info, textureLocation));
            });
        }

        T data = createDataHolder(info, textureLocation);
        data.setStatus(DownloadStatus.COMPLETED);
        cache.put(info.base(), data);
        return data;
    }

    private boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.length() <= 16 && name.matches("[a-zA-Z0-9_]+");
    }

    @Override
    public boolean delete(NameInfo info) {
        return true;
    }

    @Override
    public HashMap<NameInfo, T> getAll() {
        return new HashMap<>();
    }

    @Override
    public void store(NameInfo info, T data) {

    }

    @Override
    public void invalidateData(NameInfo info) {
        cache.remove(info.base());
    }

    @Override
    public void discover() {
        List<Path> files = getCacheFiles();
        for (Path file : files) {
            String fileName = file.getFileName().toString();
            String baseName = fileName.substring(0, fileName.length() - 4);
            NameInfo info = NameInfo.parse(baseName);
            if (info.isEmpty()) {
                PlayerArmorStandsClient.LOGGER.warn("Cannot parse NameInfo from cached file name: " + fileName);
                continue;
            }
            ResourceLocation texture = InfoTranslators.getInstance()
                    .toResourceLocation(getDataHolderClass(), info);
            TextureUtils.registerTexture(file, texture, true);
            cache.put(info.base(), createDataHolder(info, texture));
            getDataManager().store(info, createDataHolder(info, texture));
        }
    }

    private List<Path> getCacheFiles() {
        try {
            return List.of(getCachePath().toFile().listFiles()).stream()
                    .filter(file -> file.isFile() && file.getName().endsWith(".png"))
                    .map(file -> file.toPath())
                    .toList();
        } catch (Exception e) {
            PlayerArmorStandsClient.LOGGER.error("Error listing cache files in " + getCachePath(), e);
            return List.of();
        }
    }

    protected abstract Path getFilePath(NameInfo info);
    protected abstract Path getCachePath();
    protected abstract T createDataHolder(NameInfo info, ResourceLocation texture);
    protected abstract DataRepository<T> getDataManager();
    protected abstract String getProviderCode();
    protected abstract Class<? extends DataHolder> getDataHolderClass();
}
