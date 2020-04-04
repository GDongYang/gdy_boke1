package com.gdy.boke.constant;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 */
@Data
public class ResultData implements Serializable{

    private static final long serialVersionUID = 1L;

    private String code;

    private String msg;

    private Object data;

    public static ResultData success(Object data) {
        return resultData(ResultCode.SUCCESS.val(), ResultCode.SUCCESS.msg(), data);
    }

    public static ResultData success(String msg){
        return resultData(ResultCode.SUCCESS.val(),msg,null);
    }

    public static ResultData success(Object data, String msg) {
        return resultData(ResultCode.SUCCESS.val(), msg, data);
    }

    public static ResultData fail(String code, String msg) {
        return resultData(code, msg, null);
    }

    public static ResultData fail(String code, String msg, Object data) {
        return resultData(code, msg, data);
    }

    private static ResultData resultData(String code, String msg, Object data) {
        ResultData resultData = new ResultData();
        resultData.setCode(code);
        resultData.setMsg(msg);
        resultData.setData(data);
        return resultData;
    }
}