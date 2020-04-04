package com.gdy.boke.constant;

public class LoginException extends RuntimeException {

    private String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public LoginException(String errMsg) {
        this.errMsg = errMsg;
    }
}
