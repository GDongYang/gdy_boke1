package com.gdy.boke.service.impl;

import com.gdy.boke.constant.KaptchaIncorrectException;
import com.gdy.boke.constant.KaptchaNotFoundException;
import com.gdy.boke.constant.KaptchaRenderException;
import com.gdy.boke.constant.KaptchaTimeoutException;
import com.gdy.boke.service.Kaptcha;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_DATE;
import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

/**
 * •谷歌默认验证码组件
 * •@className: GoogleKaptcha
 *
 */
@Slf4j
public class GoogleKaptcha implements Kaptcha {

    private DefaultKaptcha kaptcha;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    public GoogleKaptcha(DefaultKaptcha kaptcha) {
        this.kaptcha = kaptcha;
    }

    @Override
    public String render() {
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate");
        response.addHeader(HttpHeaders.CACHE_CONTROL, "post-check=0, pre-check=0");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.setContentType("image/jpeg");
        String sessionCode = kaptcha.createText();
        try (ServletOutputStream out = response.getOutputStream()) {
            request.getSession().setAttribute(KAPTCHA_SESSION_KEY, sessionCode);
            request.getSession().setAttribute(KAPTCHA_SESSION_DATE, System.currentTimeMillis());
            ImageIO.write(kaptcha.createImage(sessionCode), "jpg", out);
            return sessionCode;
        } catch (IOException e) {
            throw new KaptchaRenderException(e);
        }
    }

    @Override
    public boolean validate(String code) {
        return validate(code, 900);
    }

    @Override
    public boolean validate(@NonNull String code, long second) {
        HttpSession httpSession = request.getSession(false);
        String sessionCode;
        if (httpSession != null && (sessionCode = (String) httpSession.getAttribute(KAPTCHA_SESSION_KEY)) != null) {
            if (sessionCode.equalsIgnoreCase(code)) {
                long sessionTime = (long) httpSession.getAttribute(KAPTCHA_SESSION_DATE);
                long duration = (System.currentTimeMillis() - sessionTime) / 1000;
                if (duration < second) {
                    httpSession.removeAttribute(KAPTCHA_SESSION_KEY);
                    httpSession.removeAttribute(KAPTCHA_SESSION_DATE);
                    return true;
                } else {
                    throw new KaptchaTimeoutException();
                }
            } else {
                throw new KaptchaIncorrectException();
            }
        } else {
            throw new KaptchaNotFoundException();
        }
    }
}
