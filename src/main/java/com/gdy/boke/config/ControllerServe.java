package com.gdy.boke.config;

import com.gdy.boke.constant.LoginException;
import com.gdy.boke.constant.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerServe {

    private static final Logger log = LoggerFactory.getLogger(ControllerServe.class);

    @ExceptionHandler
    @ResponseBody
    public ResultData serveException(LoginException ex){
        log.info(ex.getErrMsg());
        return new ResultData().fail("20001",ex.getErrMsg());
    }

}
