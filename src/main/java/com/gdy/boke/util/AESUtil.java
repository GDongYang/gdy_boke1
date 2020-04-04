package com.gdy.boke.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);

    private static final String KEY_ALGORITHM = "AES";
    private static final String CHAR_SET = "UTF-8";
    /**
     * AES的密钥长度
     */
    private static final Integer SECRET_KEY_LENGTH = 128;
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            //创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes(CHAR_SET);
            //初始化为加密密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
            byte[] encryptByte = cipher.doFinal(byteContent);
            return Base64.encodeBase64String(encryptByte);
        } catch (Exception e) {
            //LOGGER.error("AES encryption operation has exception,content:{},password:{}", content, password, e);
        }
        return null;
    }

    /**
     * AES解密操作
     *
     * @param encryptContent 加密的密文
     * @param password       解密的密钥
     * @return
     */
    public static String decrypt(String encryptContent, String password) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行解密操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(encryptContent));
            return new String(result, CHAR_SET);
        } catch (Exception e) {
            //LOGGER.error("AES decryption operation has exception,content:{},password:{}", encryptContent, password, e);
        }
        return null;
    }

    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
        //生成指定算法密钥的生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(SECRET_KEY_LENGTH, new SecureRandom(password.getBytes()));
        //生成密钥
        SecretKey secretKey = keyGenerator.generateKey();
        //转换成AES的密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }

    public static void main(String[] args) throws Exception {
//        String encryptStr = "fZuB2BiVQlkZfRbKT9pKyagzJHMkW3hUZ4RnTAmgKMKvCAm1rvegmeoxLute4ovexnoKpIe88StzHZ/YYTKRCkT9vKhSEbmX8pSSnB4Rx+3bpxhtbkCayRnow2ArMvNDO3/zGmrP+iVAhtsNOA8i7lP+1lsOyXJo/TuZnCttO609Izk4htgDRCAW5QHIGuVGpP5EG4+vfzlQ/e3DFk+BM8bjJ8PGDRrr8ldWf0sp43UB5wYdjNro/F1ZSEqPYiTjN7jH42VuBcv1HZzvpHfOjg==";
//        encryptStr = encryptStr.replaceAll(" ", "+");
//        System.out.println(encryptStr);
////        String encryptStr = encrypt(str, "KamfuIDDataSign");
////        System.out.println("encrypt:" + encryptStr);
        String str = "{\"idcode\":\"330328199503256113\",\"username\":\"朱炜明\",\"address\":\"广东省湛江市廉江市青平镇\",\"nation\":\"汉族\",\"department\":\"廉江市公安局\",\"startDate\":\"20160905\",\"endDate\":\"20260905\"}";
//        System.out.println(encrypt(str, "KamfuIDDataSign"));
//        String decryptStr = decrypt(encryptStr, "KamfuIDDataSign");
        String aaa = encrypt(str, "KamfuIDDataSign");
        System.out.println(aaa);
//        System.out.println("decryptStr:" + decryptStr);
    }
}
