package com.danrus.utils;

public class StringUtils {
    public static String encodeToBase64(String source) {
        try {
            return java.util.Base64.getEncoder().encodeToString(source.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decodeBase64(String base64) {
        try {
            return new String(java.util.Base64.getDecoder().decode(base64), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeToSha256(String source) {
        try {
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
            e.printStackTrace();
            return null;
        }
    }
}
