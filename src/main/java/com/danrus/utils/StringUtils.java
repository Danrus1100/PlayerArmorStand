package com.danrus.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            e.printStackTrace();
            return null;
        }
    }

    public static @NotNull List<String> matchASName(String input){
        String regex = "^([^|]+)(?:\\|([NSC]{1,3})(?:(?<=C):([^|]+))?)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String name = matcher.group(1).trim();
            String params = matcher.group(2) != null ? matcher.group(2).trim() : "";
            String id = matcher.group(3) != null ? matcher.group(3).trim() : "";

            return List.of(name, params, id);
        } else {
            return List.of(input.trim(), "", "");
        }
    }
}
