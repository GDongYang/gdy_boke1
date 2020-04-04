package com.gdy.boke.util;

public class HttpClientResoult {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     */
    private int code;
    /**
     * 响应数据
     */
    private String content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HttpClientResoult(int code, String content) {
        this.code = code;
        this.content = content;
    }
}
