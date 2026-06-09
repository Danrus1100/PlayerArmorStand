package com.danrus.pas.impl.data.common;

import com.danrus.pas.api.data.DataHolder;
import com.danrus.pas.api.data.DataRepository;
import com.danrus.pas.api.data.DataType;
import com.danrus.pas.api.info.NameInfo;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Configurable disk data provider. Replaces the per-source subclasses
 * (Mojang/Namemc/MinecraftCapes skin & cape variants) with a single
 * parameterized implementation.
 */
public class DiskDataProvider<T extends DataHolder> extends AbstractDiskDataProvider<T> {

    private final String name;
    private final DataType dataType;
    private final Class<? extends DataHolder> holderClass;
    private final Supplier<T> holderFactory;
    private final Supplier<DataRepository<T>> dataManager;
    private final boolean processSkin;
    private final Predicate<NameInfo> getFilter;

    public DiskDataProvider(
            String name,
            DataType dataType,
            Class<? extends DataHolder> holderClass,
            Supplier<T> holderFactory,
            Supplier<DataRepository<T>> dataManager,
            boolean processSkin,
            Predicate<NameInfo> getFilter) {
        this.name = name;
        this.dataType = dataType;
        this.holderClass = holderClass;
        this.holderFactory = holderFactory;
        this.dataManager = dataManager;
        this.processSkin = processSkin;
        this.getFilter = getFilter;
    }

    @Override
    public Optional<T> get(NameInfo info) {
        if (getFilter != null && !getFilter.test(info)) {
            return Optional.empty();
        }
        return super.get(info);
    }

    @Override
    protected T createDataHolder() {
        return holderFactory.get();
    }

    @Override
    protected DataRepository<T> getDataManager() {
        return dataManager.get();
    }

    @Override
    protected Class<? extends DataHolder> getDataHolderClass() {
        return holderClass;
    }

    @Override
    protected boolean shouldProcessSkin() {
        return processSkin;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }
}
