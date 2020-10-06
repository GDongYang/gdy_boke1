package com.gdy.boke.util;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSASecrteKey {
    public static final int KEY_SIZE = 1024;

    public RSASecrteKey() {
    }

    public static RSAKeys generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        RSAKeys keys = new RSAKeys();
        keys.setPrivateKey(Base64.encode(privateKey.getEncoded()));
        keys.setPublicKey(Base64.encode(publicKey.getEncoded()));
        return keys;
    }
}
