package com.gdy.boke.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class AESUtilSammbo {
    private static final Logger logger = LoggerFactory.getLogger(AESUtil.class);
    private static final String sKey = "CD6F7D08DF62341A";
//    private static final String sKey = "CD7F7D09DF75502B";

    // 加密
    public static String encrypt(String sSrc) throws Exception {
        if (sKey == null) {
            logger.info("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            logger.info("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes(StandardCharsets.UTF_8));//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(encrypted);//此处使用BAES64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String decrypt(String sSrc) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                logger.info("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                logger.info("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes(StandardCharsets.US_ASCII);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decodeBase64(sSrc);//先用bAES64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, StandardCharsets.UTF_8);
                return originalString;
            } catch (Exception e) {

                logger.info(e.toString());
                return null;
            }
        } catch (Exception ex) {
            logger.info(ex.toString());
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String s = AESUtilSammbo.encrypt("2601323022752112640");
        System.out.println(s);
    }

    /**
     * 视频校验
     * @param mtUrl
     */
    public static void checkVideo(String mtUrl) {
        String extUpp = null;
        if (mtUrl != null) {
            extUpp = org.apache.commons.lang3.StringUtils.
                    upperCase(mtUrl.substring(mtUrl.lastIndexOf(".") + 1));
        }
        //根据扩展名判断是否为要求的视频
        if (!extUpp.matches("^[(MP4)|(AVI)]+$")) {
            System.out.println("error");
        }
    }
}
