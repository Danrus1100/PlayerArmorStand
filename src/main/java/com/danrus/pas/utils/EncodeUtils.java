package com.danrus.pas.utils;

import com.danrus.pas.PlayerArmorStandsClient;

import java.util.Base64;

public class EncodeUtils {
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
            return new String(decodeBase64Bytes(base64), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            PlayerArmorStandsClient.LOGGER.warn("Failed to decode Base64 string: {}", base64, e);
            return null;
        }
    }

    public static byte[] decodeBase64Bytes(String base64) {
        return Base64.getDecoder().decode(base64);
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

}
