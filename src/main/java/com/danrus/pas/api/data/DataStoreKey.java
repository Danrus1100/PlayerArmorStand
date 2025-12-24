package com.danrus.pas.api.data;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.impl.features.CapeFeature;
import com.danrus.pas.impl.features.SkinProviderFeature;

import java.util.Objects;

/**
 * Immutable key for data storage.
 * Combines base name, provider, and data type to ensure uniqueness.
 */
public final class DataStoreKey {

    private final String baseName;
    private final String provider;
    private final DataType dataType;

    private final String nameForRestore;

    public DataStoreKey(String baseName, String provider, DataType dataType, String nameForRestore) {
        this.baseName = baseName;
        this.provider = provider;
        this.dataType = dataType;
        this.nameForRestore = nameForRestore;
    }



    /**
     * Creates a key for skin data
     */
    public static DataStoreKey forSkin(NameInfo info) {
        return new DataStoreKey(
                info.base(),
                info.getFeature(SkinProviderFeature.class).getProvider(),
                DataType.SKIN,
                info.compile()
        );
    }

    public static DataStoreKey parsePrototype(String input) {
        String[] parts = input.split("-", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid DataStoreKey format: " + input);
        }
        String baseName = parts[0];
        String provider = parts[1];
        DataType dataType = DataType.valueOf(parts[2]);
        return new DataStoreKey(baseName, provider, dataType, "");
    }

    /**
     * Creates a key for cape data
     */
    public static DataStoreKey forCape(NameInfo info) {
        return new DataStoreKey(
                info.getFeature(CapeFeature.class).getId().isEmpty() ? info.base() : info.getFeature(CapeFeature.class).getId(),
                info.getFeature(CapeFeature.class).getProvider(),
                DataType.CAPE,
                info.compile()
        );
    }

    public NameInfo tryToNameInfo() {
        return NameInfo.parse(nameForRestore);
    }

    /**
     * Returns string representation for serialization
     */
    public String asString() {
        return baseName + "-" + provider + "-" + dataType.name();
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseName, provider, dataType);
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
