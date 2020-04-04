package com.gdy.boke.test;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestChange {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<ServiceObjectEnum> list = new ArrayList<>();
        list.add(ServiceObjectEnum.QTZZ);
        list.add(ServiceObjectEnum.QYFR);
        list.add(ServiceObjectEnum.SHZZFR);
       /* String strip = StringUtils.strip(list.stream().map(ServiceObjectEnum::getCode).collect(Collectors.toList()).toString(), "[]");
     */
        //调用
        String strip = change(list,ServiceObjectEnum.class);
        strip = strip.replace(",","^");
        System.out.println(strip);
    }

    public static  <T>  String  change(List objEnums,Class clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //获取指定方法
        Method getCode = clazz.getMethod("getCode");
        ArrayList<Object> list = new ArrayList<>();
        for(Object obj: objEnums){
           String code = getCode.invoke(obj).toString();
            list.add(code);
        }
        String strip = StringUtils.strip(list.toString(), "[]");
        return strip;
    }

}
