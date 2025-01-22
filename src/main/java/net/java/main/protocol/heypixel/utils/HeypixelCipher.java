package net.java.main.protocol.heypixel.utils;



import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;


public class HeypixelCipher extends Cipher {
    public HeypixelCipher(CipherSpi cipherSpi, Provider provider, String str) {
        super(cipherSpi, provider, str);
    }


    public static Cipher get(String str) throws NoSuchPaddingException, NoSuchAlgorithmException {
        return getInstance(str);
    }
}
