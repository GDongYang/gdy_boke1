package com.gdy.boke.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class FileUtil {


    private static final List<String> FILE_TYPE = Arrays.asList("jpg","png","txt");



    public static String urlToBase6(String url) throws Exception {
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        URL fileUrl = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) fileUrl.openConnection();
        //设置连接
        urlConnection.setRequestMethod("get");
        urlConnection.setConnectTimeout(10000);
        urlConnection.setRequestProperty("charset","utf-8");
        if(urlConnection.getResponseCode()!=200){
            throw new Exception("下载文件失败");
        }
        InputStream is = urlConnection.getInputStream();
        byte[] data = new byte[1024];
        int len = -1;
        while((len=is.read(data))!=-1){
            outPut.write(data,0,len);
        }

        return new BASE64Encoder().encode(outPut.toByteArray());
    }

    /**
     * 校验文件类型是否被允许
     */
    public static boolean permissionFile(String fileName){

        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(FILE_TYPE.contains(fileType)){
            return true;
        }
        return false;
    }

    /**
     * 将文件转为base64编码字符串
     */
    public static String fileToBase64(String path) throws IOException {
        File file = new File(path);
        InputStream is = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        is.read(data);
        is.close();
        return new BASE64Encoder().encode(data);
    }

    /**
     * 将base64编码字符串转为文件
     */
    public static void Base64ToFile(String fileStr,String filePath) throws IOException {
        byte[] bytes = new BASE64Decoder().decodeBuffer(fileStr);
        File file = new File(filePath);
        OutputStream out = new FileOutputStream(file);
        out.write(bytes);
        out.close();
    }

}
