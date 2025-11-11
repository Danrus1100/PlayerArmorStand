package com.danrus.pas.api.data;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.features.SkinProviderFeature;

import java.util.Objects;

/**
 * Immutable key for data storage.
 * Combines base name, provider, and data type to ensure uniqueness.
 */
public record DataStoreKey(String baseName, String provider, DataType dataType) {

    /**
     * Creates a key for skin data
     */
    public static DataStoreKey forSkin(NameInfo info) {
        return new DataStoreKey(
                info.base(),
                info.getFeature(SkinProviderFeature.class).getProvider(),
                DataType.SKIN
        );
    }

    /**
     * Creates a key for cape data
     */
    public static DataStoreKey forCape(NameInfo info) {
        return new DataStoreKey(
                info.getFeature(CapeFeature.class).getId().isEmpty() ? info.base() : info.getFeature(CapeFeature.class).getId(),
                info.getFeature(CapeFeature.class).getProvider(),
                DataType.CAPE
        );
    }

    /**
     * Creates a key from string (for serialization)
     */
    public static DataStoreKey fromString(String key) {
        String[] parts = key.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid key format: " + key);
        }
        return new DataStoreKey(parts[0], parts[1], DataType.valueOf(parts[2]));
    }


    // TODO: Hacky way to convert back to NameInfo
    public NameInfo toNameInfo() {
        NameInfo info = new NameInfo(baseName);

        switch (dataType) {
            case SKIN -> {
                SkinProviderFeature skinFeature = info.getFeature(SkinProviderFeature.class);
                if (skinFeature != null) {
                    skinFeature.setProvider(provider);
                }
            }
            case CAPE -> {
                CapeFeature capeFeature = info.getFeature(CapeFeature.class);
                if (capeFeature != null) {
                    capeFeature.setId(baseName);
                    info.setName(baseName);
                    capeFeature.setEnabled(true);
                }
            }
        }

        return info;
    }

    /**
     * Returns string representation for serialization
     */
    public String asString() {
        return baseName + ":" + provider + ":" + dataType.name();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DataStoreKey other)) return false;

        return Objects.equals(baseName, other.baseName)
                && Objects.equals(provider, other.provider)
                && dataType == other.dataType;
    }

    @Override
    public String toString() {
        return "DataStoreKey[" + asString() + "]";
    }

    /**
     * Data type enum to prevent conflicts between repositories
     */
    public enum DataType {
        SKIN,
        CAPE,
        ELYTRA
    }
}
