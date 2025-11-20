package com.danrus.pas.utils;

import com.danrus.pas.api.info.NameInfo;
import com.google.gson.Gson;

import java.util.concurrent.CompletableFuture;

public class MojangUtils {
    private static final String MOJANG_API_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String USERNAME_PATTERN = "[a-zA-Z0-9_]+";
    private static final int MAX_USERNAME_LENGTH = 16;
    private static final Gson gson = new Gson();

    public static boolean isNicknameValid(String nickname) {
        return nickname != null && !nickname.isEmpty() && nickname.length() <= MAX_USERNAME_LENGTH && nickname.matches(USERNAME_PATTERN);
    }

    public static CompletableFuture<String> getUUID(NameInfo info) {
        return RestHelper.get(MOJANG_API_URL + info.base())
                .thenCompose(response -> processSimpleProfile(response, info));
    }

    private static CompletableFuture<String> processSimpleProfile(String response, NameInfo info) {
        SimpleProfile simpleProfile = gson.fromJson(response, SimpleProfile.class);
        if (simpleProfile == null || simpleProfile.id == null) {
            return CompletableFuture.failedFuture(new RuntimeException("Invalid simple profile"));
        }
        return CompletableFuture.completedFuture(simpleProfile.id);
    }

    static class SimpleProfile {
        public String id;
        public String name;
    }
}
