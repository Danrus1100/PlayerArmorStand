package com.danrus.api;

import com.danrus.PlayerArmorStands;
import com.danrus.utils.RestHelper;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class AbstractServerApi {

    public class SimpleProfile {
        public String id;
        public String name;
    }

    public class Profile {
        public String id;
        public String name;
        public List<Property> properties;

        public static class Property {
            public String name;
            public String value;
        }
    }

    private static <T> T doGetRequestWithFormatedResponse(String parameter, String entryPoint, Class<T> responseType) throws ExecutionException, InterruptedException {
        String responseString = RestHelper.get(entryPoint + parameter).get();
        if (responseString == null || responseString.isEmpty()) {
            return null;
        }
        return PlayerArmorStands.GSON.fromJson(responseString, responseType);
    }

    public static SimpleProfile getProfileDataByName(String username, String entryPoint) throws ExecutionException, InterruptedException {
        return doGetRequestWithFormatedResponse(username, entryPoint, SimpleProfile.class);
    }

    public static Profile getTexturedDataByUUID(String uuid, String entryPoint) throws ExecutionException, InterruptedException {
        return doGetRequestWithFormatedResponse(uuid, entryPoint, Profile.class);
    }
}
