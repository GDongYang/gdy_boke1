package com.gdy.boke.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static void main(String[] args) throws ParseException {
        String time1 =  "2018-03-01 12:00";
        String time2 = "2018-03-01 12:30";
        boolean b = compareTime(time1, time2);
        System.out.println(b);
    }

    /**
     * 比较时间是否在一小时内
     * @param time1
     * @param time2
     * @return
     */
    public static boolean compareTime(String time1,String time2) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);
        long longTime1 = date1.getTime();
        long longTime2 = date2.getTime();
        //计算小时差
        int hours = (int) ((longTime2-longTime1)/(1000*60*60));
        if(hours<1)
            return true;
        return false;
    }

}
