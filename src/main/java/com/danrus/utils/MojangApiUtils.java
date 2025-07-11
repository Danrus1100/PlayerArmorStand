package com.danrus.utils;

import com.danrus.PASClient;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class MojangApiUtils {
    private static final String MOJANG_API_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final Gson gson = new Gson();
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 2000;

    public static CompletableFuture<String> getUUID(String name) {
        return RestHelper.get(MOJANG_API_URL + name)
                .thenApply((response) -> {
                    PASClient.LOGGER.debug(response);
                    try {
                        Profile profile = gson.fromJson(response, Profile.class);
                        if (profile != null && profile.id != null) {
                            return profile.id;
                        } else {
                            PASClient.LOGGER.warn("MojangApiUtils: No UUID found for player " + name);
                            return null;
                        }
                    } catch (JsonParseException e) {
                        PASClient.LOGGER.error("MojangApiUtils: Failed to parse response for " + name, e);
                        return null;
                    }
                });
    }



    public static class Profile {
        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;
    }
}