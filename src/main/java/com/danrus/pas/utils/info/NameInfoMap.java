package com.danrus.pas.utils.info;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoLike;
import com.danrus.pas.api.info.NameInfoPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NameInfoMap<T> implements Map<NameInfo, T> {
    private final Map<NameInfo, T> map;

    public NameInfoMap() {
        this.map = new HashMap<>();
    }

    public NameInfoMap(Map<NameInfo, T> backend) {
        this.map = backend;
    }

    public NameInfoMap(NameInfoMap<T> copy) {
        this.map = Map.copyOf(copy);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public T get(Object key) {
        return map.get(key);
    }

    @Override
    public @Nullable T put(NameInfo key, T value) {
        return map.put(key, value);
    }

    @Override
    public T remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends NameInfo, ? extends T> source) {
        map.putAll(source);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public @NotNull Set<NameInfo> keySet() {
        return map.keySet();
    }

    @Override
    public @NotNull Collection<T> values() {
        return map.values();
    }

    @Override
    public @NotNull Set<Entry<NameInfo, T>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object object) {
        return map.equals(object);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    public boolean containsMatch(@NotNull NameInfoLike query) {
        return findFirst(query).isPresent();
    }

    public @NotNull Optional<Entry<NameInfo, T>> findFirst(@NotNull NameInfoLike query) {
        NameInfoPattern pattern = Objects.requireNonNull(query, "query").toPattern();
        return map.entrySet().stream()
                .filter(e -> pattern.matches(e.getKey()))
                .findFirst();
    }

    public @NotNull Map<NameInfo, T> findAll(@NotNull NameInfoLike query) {
        NameInfoPattern pattern = Objects.requireNonNull(query, "query").toPattern();
        Map<NameInfo, T> result = new LinkedHashMap<>();
        for (Entry<NameInfo, T> entry : map.entrySet()) {
            if (pattern.matches(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public int deleteAll(@NotNull NameInfoLike query) {
        NameInfoPattern pattern = Objects.requireNonNull(query, "query").toPattern();
        int sizeBefore = map.size();
        map.keySet().removeIf(pattern);
        return sizeBefore - map.size();
    }
}
