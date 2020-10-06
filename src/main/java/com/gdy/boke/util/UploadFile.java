package com.gdy.boke.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadFile {


    public static void main(String[] args) {
        String muchText = "<span style=\"text-decoration-line: underline;\">asdasdas<span style=\"font-weight: bold;\">asdasdasdsaasvadvas</span></span>";
        muchText = dealContent(muchText);
        System.out.println(muchText);
    }

    public static String dealContent(String content) {
        String regx = "(<.+?>)|(</.+?>)";
        Matcher matcher = Pattern.compile(regx).matcher(content);
        while (matcher.find()) {
            // 替换图片
            content = matcher.replaceAll("").replace(" ", "");
        }

        return content;
    }

    public static String getZh(String str) {
        String regx = "([\u4e00-\u9fa5]+)";

        String zh = "";

        Matcher matcher = Pattern.compile(regx).matcher(str);
        while (matcher.find()) {
            zh += matcher.group(0);
        }
        return zh;
    }

}
