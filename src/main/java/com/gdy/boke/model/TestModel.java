package com.gdy.boke.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestModel {

    private Date createDate;

    private Long createDateLong;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateDateLong() {
        if(createDate!=null){
            return createDate.getTime();
        }
        return createDateLong;
    }

    public void setCreateDateLong(Long createDateLong) {
        this.createDateLong = createDateLong;
    }

    public static void main(String[] args) {

    }
    /**
     * 动态规划最优解  1天2    7天7   30天15
     */
    public static int getMin(int[] days,int[] costs){
        Map<Integer,Integer> map = new LinkedHashMap();
        int cost = 0;
        int day = days[0];
        map.put(day,cost);
        for(int i = 0;i<days.length;i++){
            if(days[i]<day+3){
                cost = map.get(days[i-1]);
                cost += 2;
                map.put(days[i],cost);
            }
        }
        return  0;
    }
}
