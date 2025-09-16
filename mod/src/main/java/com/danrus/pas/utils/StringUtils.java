package com.danrus.pas.utils;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.PasApi;
import com.danrus.pas.api.feature.PasFeature;
import com.danrus.pas.api.request.RequestData;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StringUtils {
    public static String encodeToBase64(String source) {
        try {
            return java.util.Base64.getEncoder().encodeToString(source.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            PlayerArmorStandsClient.LOGGER.warn("Failed to encode string to Base64: {}", source, e);
            return null;
        }
    }

    public static String decodeBase64(String base64) {
        try {
            return new String(java.util.Base64.getDecoder().decode(base64), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            PlayerArmorStandsClient.LOGGER.warn("Failed to decode Base64 string: {}", base64, e);
            return null;
        }
    }

    public static String encodeToSha256(String source) {
        try {
            source = source.toLowerCase();
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(source.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.io.UnsupportedEncodingException | java.security.NoSuchAlgorithmException e) {
            PlayerArmorStandsClient.LOGGER.warn("Failed to encode string to SHA-256: {}", source, e);
            return null;
        }
    }

    public static String getBaseName(String string) {
        String[] divided = string.split("\\|");
        return divided[0].trim();
    }

    @NotNull
    public static ResourceLocation getSkinCacheLocation(String string) {
        Map<Class<? extends PasFeature>, Function<String, String>> getters = PasApi.getFeatureRegistry().getLocationsGetters();
        String result;
        for (Function<String, String> getter : getters.values()) {
            result = getter.apply(string);
            if (!result.isEmpty()) {
                String[] split = result.split(":");
                return VersioningUtils.getResourceLocation(split[0], split[1]);
            }
        }
        return VersioningUtils.getResourceLocation("");
    }
}
