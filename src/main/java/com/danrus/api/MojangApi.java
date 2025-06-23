package com.danrus.api;

import com.danrus.PlayerArmorStands;
import com.danrus.utils.StringUtils;

import java.util.concurrent.ExecutionException;

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
            }
        }
    }

    public static String getSkinUrl(String playerName) throws ExecutionException, InterruptedException {
        String uuid = getProfileDataByName(playerName, "https://api.mojang.com/users/profiles/minecraft/").id;
        Profile profile = getTexturedDataByUUID(uuid, "https://sessionserver.mojang.com/session/minecraft/profile/");
        try {
            SkinData data = PlayerArmorStands.GSON.fromJson(StringUtils.decodeBase64(profile.properties.getFirst().value), SkinData.class);
            return data.textures.SKIN.url;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isValidUsername(String username) {
        return username != null && !username.isEmpty() && username.length() <= 16 && username.matches("[a-zA-Z0-9_]+");
    }
}
