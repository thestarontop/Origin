package net.java.main.protocol.heypixel.utils;



import javax.crypto.KeyGenerator;
import javax.crypto.KeyGeneratorSpi;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;



public class HeypixelKeyGenerator extends KeyGenerator {

    public HeypixelKeyGenerator(KeyGeneratorSpi keyGeneratorSpi, Provider provider, String str) {
        super(keyGeneratorSpi, provider, str);
    }


    public static HeypixelKeyGenerator fromString(String str) throws NoSuchAlgorithmException {
        return (HeypixelKeyGenerator) KeyGenerator.getInstance(str);
    }
}
