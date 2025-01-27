package net.java.main.protocol.heypixel.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class StringUtils {
    public static String hashString(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString((b | (-256)) + 256);
                if (hexString.length() == 1) {
                    sb.append('0');
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String getReplaced(String str, Object... args) {
        String s = str;
        for (Object a : args) {
            s = s.replaceFirst("\\{}", a == null ? "null" : a.toString());
        }
        return s;
    }


}
