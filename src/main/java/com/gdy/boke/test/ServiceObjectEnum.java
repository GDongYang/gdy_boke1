package com.gdy.boke.test;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ServiceObjectEnum {
    ZRE("1","自然人"),
    QYFR("2","企业法人"),
    SYFR("3","事业法人"),
    SHZZFR("4","社会组织法人"),
    FFRQY("5","非法人企业"),
    XZJG("6","行政机关"),
    QTZZ("9","其他组织");
    @JsonValue    //标记响应json值
    private String code;
    private String value;

    ServiceObjectEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return code;
    }
}
