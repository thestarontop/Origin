package net.java.main.protocol.heypixel.check;





public class Base64 {

    public static String encodeToString(byte[] bArr) {
        return java.util.Base64.getEncoder().encodeToString(bArr);
    }

    public static byte[] deocde(String str) {
        return java.util.Base64.getDecoder().decode(str);
    }
}
