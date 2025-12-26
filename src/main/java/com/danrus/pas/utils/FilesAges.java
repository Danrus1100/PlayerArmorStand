package com.danrus.pas.utils;

import com.danrus.pas.config.SkinReloadTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FilesAges {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Long>>() {}.getType();

    private final Path file;
    private final Map<String, Long> ages = new HashMap<>();

    public FilesAges(Path file) {
        this.file = file;
        load();
    }


    public static int millisFromSeconds(int seconds) {
        return seconds * 1000;
    }

    public static int millisFromMinutes(int minutes) {
        return minutes * 60 * 1000;
    }

    public static int millisFromHours(int hours) {
        return hours * 60 * 60 * 1000;
    }

    public static int millisFromDays(int days) {
        return days * 24 * 60 * 60 * 1000;
    }

    public static int millisFromSkinReloadTime(SkinReloadTime time){
        return switch (time) {
            case NEVER -> Integer.MAX_VALUE;
            case HOUR_12 -> millisFromHours(1);
            case DAY_1 -> millisFromDays(1);
            case DAY_3 -> millisFromDays(3);
            case DAY_7 -> millisFromDays(7);
        };
    }

    private void load() {
        if (!Files.exists(file)) return;

        try {
            String json = Files.readString(file);
            Map<String, Long> loaded = GSON.fromJson(json, MAP_TYPE);
            if (loaded != null) {
                ages.putAll(loaded);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(ages));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void touch(String key) {
        ages.put(key, System.currentTimeMillis());
        save();
    }

    public long ageMillis(String key) {
        Long time = ages.get(key);
        if (time == null) return Long.MAX_VALUE;
        return System.currentTimeMillis() - time;
    }

    public long ageSeconds(String key) {
        return ageMillis(key) / 1000;
    }

    public boolean isExpired(String key, long maxAgeMillis) {
        return ageMillis(key) > maxAgeMillis;
    }

    public void remove(String key) {
        ages.remove(key);
        save();
    }

    public boolean contains(String key) {
        return ages.containsKey(key);
    }
}
