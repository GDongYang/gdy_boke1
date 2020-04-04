package com.gdy.boke.constant;

/**
 * •Kaptcha验证码异常基类
 * •@className: KaptchaException
 * •@author: 吕自聪
 */
public class KaptchaException extends RuntimeException {

    public KaptchaException() {
        super();
    }

    public KaptchaException(Throwable e) {
        super(e);
    }
}