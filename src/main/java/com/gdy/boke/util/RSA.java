package com.gdy.boke.util;


import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    public RSA() {
    }

    public static String sign(String content, String privateKey, String input_charset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            signature.update(content.getBytes(input_charset));
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static boolean verify(String content, String sign, String publicKey, String input_charset) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes(input_charset));
            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;
        } catch (Exception var9) {
            return false;
        }
    }

    public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
        byte[] bytes = encryptByPrivateKey(data.getBytes(), privateKey);
        return Base64.encode(bytes);
    }

    public static String encryptByPublicKey(String data, String publicKey) throws Exception {
        byte[] bytes = encryptByPublicKey(data.getBytes(), publicKey);
        return Base64.encode(bytes);
    }

    public static String decryptByPrivateKey(String base64_data, String privateKey) throws Exception {
        byte[] data = Base64.decode(base64_data);
        return new String(decryptByPrivateKey(data, privateKey));
    }

    public static String decryptByPublicKey(String base64_data, String publicKey) throws Exception {
        byte[] data = Base64.decode(base64_data);
        return new String(decryptByPublicKey(data, publicKey));
    }

    private static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for(int i = 0; inputLen - offSet > 0; offSet = i * 128) {
            byte[] cache;
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedData, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    private static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for(int i = 0; inputLen - offSet > 0; offSet = i * 128) {
            byte[] cache;
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedData, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    private static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for(int i = 0; inputLen - offSet > 0; offSet = i * 117) {
            byte[] cache;
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    private static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for(int i = 0; inputLen - offSet > 0; offSet = i * 117) {
            byte[] cache;
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    private static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    private static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = Base64.decode(key);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        return publicKey;
    }

    public static void main(String[] args) throws Exception {
        RSAKeys keysPair = RSASecrteKey.generateKeyPair();
        String privateKey = keysPair.getPrivateKey();
        String publicKey = keysPair.getPublicKey();
        System.out.println("privateKey : " + privateKey);
        System.out.println("publicKey : " + publicKey);
        String userId = "13300000001";
        int ttl = 300;
        Long start = System.currentTimeMillis();
        String content = userId + "_" + start + "_" + ttl;
        String sign = sign(content, privateKey, "utf-8");
        System.out.println("sign: " + sign);
        System.out.println("verify : " + verify(content, sign, publicKey, "utf-8"));
        String source = "这是一串原始内容这是一串原始内容这是";
        String sign1 = sign(source, privateKey, "utf-8");
        System.out.println("sign1: " + sign1);
        System.out.println("verify1 : " + verify(source, sign1, publicKey, "utf-8"));
        source = "公钥加密，私钥解密============================";
        byte[] data = source.getBytes();
        byte[] encodedData = encryptByPublicKey(data, publicKey);
        byte[] decodedData = decryptByPrivateKey(encodedData, privateKey);
        String target = new String(decodedData);
        System.out.println("解密结果 : " + source.equals(target));
        source = "私钥加密，公钥解密++++++++++++++++++++++++++++";
        byte[] data1 = source.getBytes();
        byte[] encodedData1 = encryptByPrivateKey(data1, privateKey);
        byte[] decodedData1 = decryptByPublicKey(encodedData1, publicKey);
        String target1 = new String(decodedData1);
        System.out.println("解密结果 : " + source.equals(target1));
    }
}
