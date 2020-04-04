package com.gdy.boke.util;

public class DetectUtil {

    public static boolean noteEmpty(Object o){
        if(o!=null&&!o.equals("")){
            return true;
        }
        return false;
    }

}
