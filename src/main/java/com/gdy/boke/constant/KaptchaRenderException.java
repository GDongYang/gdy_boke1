package com.gdy.boke.constant;

import java.io.IOException;

/**
 * •Kaptcha验证码渲染失败异常
 * •@className: KaptchaRenderException
 * •@author: 吕自聪
 */
public class KaptchaRenderException extends KaptchaException {

    public KaptchaRenderException(IOException e) {
        super(e);
    }
}