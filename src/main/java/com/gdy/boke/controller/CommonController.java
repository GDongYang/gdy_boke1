package com.gdy.boke.controller;

import com.gdy.boke.constant.LoginException;
import com.gdy.boke.service.Kaptcha;
import com.gdy.boke.util.FtpOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 获取验证码,上传文件
 */
@Controller
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);



    private final Kaptcha kaptcha;

    @Autowired
    public CommonController(Kaptcha kaptcha) {
        this.kaptcha = kaptcha;
    }

    @Autowired
    private FtpOperation ftpOperation;

    @GetMapping("/render")
    @ResponseBody
    public void render() {
        kaptcha.render();
    }

    @RequestMapping("/blogPicture/uploadPicture")
    @ResponseBody
    public Map<String,Object> uploadFile(MultipartFile imgFile){
        log.info("start upload image:"+imgFile.getOriginalFilename());
        String realName = imgFile.getOriginalFilename();
        String imgName = realName.substring(0,realName.lastIndexOf("."));
        String afterfix = realName.substring(realName.lastIndexOf("."),realName.length());
        //上传服务器的文件名
        String randomName = UUID.randomUUID().toString();
        String uploadName = imgName+randomName+afterfix;
        String ftpImgName = "";
        try {
            boolean result = ftpOperation.uploadToFtp(imgFile.getInputStream(), uploadName, false);
            if(result==false){
                throw new LoginException("upload picture failed");
            }
            ftpImgName = "http://192.168.25.129:8080/"+uploadName;
            log.info("fdpImgName:"+ftpImgName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Object> he = new HashMap<>();
        he.put("url",ftpImgName);
        he.put("error", 0);
        return he;
    }
}

