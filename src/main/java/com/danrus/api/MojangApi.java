package com.danrus.api;

import com.danrus.PlayerArmorStands;
import com.danrus.utils.RestHelper;

import java.util.concurrent.CompletableFuture;

public class MojangApi extends AbstractServerApi{
    public class SkinData {
        public long timestamp;
        public String profileId;
        public String profileName;
        public boolean signatureRequired;
        public Textures textures;

        public static class Textures {
            public Skin SKIN;

            public static class Skin {
                public String url;
                public Metadata metadata;

                public static class Metadata {
                    public String model;
                }
            }
        }
    }

    public static CompletableFuture<SimpleProfile> getProfileDataByNameAsync(String username) {
        return RestHelper.get("https://api.mojang.com/users/profiles/minecraft/" + username.toLowerCase())
                .thenApply(response -> {
                    if (response == null || response.isEmpty()) {
                        return null;
                    }
                    return PlayerArmorStands.GSON.fromJson(response, SimpleProfile.class);
                });
    }

    public static CompletableFuture<Profile> getTexturedDataByUUIDAsync(String uuid) {
        return RestHelper.get("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid)
                .thenApply(response -> {
                    if (response == null || response.isEmpty()) {
                        return null;
                    }
                    return PlayerArmorStands.GSON.fromJson(response, Profile.class);
                });
    }


    public static boolean isValidUsername(String username) {
        return username != null && !username.isEmpty() && username.length() <= 16 && username.matches("[a-zA-Z0-9_]+");
    }
}
