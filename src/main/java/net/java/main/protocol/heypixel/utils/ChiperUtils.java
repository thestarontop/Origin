package net.java.main.protocol.heypixel.utils;



import net.java.main.protocol.heypixel.check.HeypixelSessionManager;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;




public class ChiperUtils {

    public static Cipher getServerCipher2(HeypixelSessionManager manager) throws NoSuchPaddingException, NoSuchAlgorithmException {
        return HeypixelCipher.get(manager.getEncryptMode1());
    }

    public static HeypixelKeyGenerator Method5165(HeypixelSessionManager manager) throws NoSuchAlgorithmException {
        return HeypixelKeyGenerator.fromString(manager.getEncryptMode1());
    }

    public static Cipher getServerCipher(HeypixelSessionManager manager) throws NoSuchPaddingException, NoSuchAlgorithmException {
        return HeypixelCipher.get(manager.getEncryptMode());
    }
}
