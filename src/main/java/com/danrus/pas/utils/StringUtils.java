package com.danrus.pas.utils;

import com.danrus.pas.PlayerArmorStandsClient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static List<String> matchTexture(String input){
        String textureRegex = "T:([A-Z0-9_]+)(?:%(\\d+))?";
        Pattern pattern = Pattern.compile(textureRegex);
        Matcher matcher = pattern.matcher(input.toUpperCase());

        if (matcher.find()) {
            String textureName = matcher.group(1).toLowerCase();
            String blendFactor = matcher.group(2) != null ? matcher.group(2) : "100";

            String params = input.replaceFirst(textureRegex, "").trim().toUpperCase();

            return List.of(textureName, blendFactor, params);
        }

        return List.of("", "", "");
    }

    public static @NotNull List<String> matchASName(String input){
        String[] divided = input.split("\\|");
        String name = divided[0].trim();
        if (name.matches(".*[<>:\"/\\\\?*].*")) {
            return List.of("", "", "", "100");
        }
        if (divided.length < 2) {
            return List.of(name, "", "", "100");
        }
        String rawParams = divided[1].toUpperCase();

        List<String> textureMatch = matchTexture(rawParams);
        if (!textureMatch.get(0).isEmpty()) {
            String textureName = textureMatch.get(0);
            String blendFactor = textureMatch.get(1);
            rawParams = textureMatch.get(2).trim().toUpperCase();
            return List.of(name, rawParams, textureName, blendFactor);
        }

        return List.of(name, rawParams.trim(), "", "100");
    }
}
