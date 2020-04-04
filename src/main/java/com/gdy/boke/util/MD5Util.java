package com.gdy.boke.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {



    public static final String encrytion(String plainText) throws NoSuchAlgorithmException {

        String res_md5 = "";
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(plainText.getBytes());
        byte[] digest = md5.digest();
        int i;
        StringBuffer buffer = new StringBuffer();
        for(int offset = 0;offset<digest.length;offset++){
            i = digest[offset];
            if(i<0)
                i+=256;
            if(i<16)
                i+=16;
                buffer.append(Integer.toHexString(i));
            buffer.append(Integer.toHexString(i));

        }
        res_md5 = buffer.toString();

        return res_md5;
    }

}
