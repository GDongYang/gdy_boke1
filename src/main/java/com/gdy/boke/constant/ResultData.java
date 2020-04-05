package com.gdy.boke.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 */
@Data
@ApiModel("统一返回类型")
public class ResultData implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="状态码")
    private String code;
    @ApiModelProperty(value="响应信息")
    private String msg;
    @ApiModelProperty(value="响应数据")
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