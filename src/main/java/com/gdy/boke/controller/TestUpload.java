package com.gdy.boke.controller;

import com.gdy.boke.util.FtpOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;

@Controller
public class TestUpload {

    @Autowired
    private FtpOperation ftpOperation;

    @RequestMapping("/uploadFile")
    public void testUpload()throws Exception {
        String fileName = "shuaige.jpg";
        File file = new File("g://111.jpg");
        FileInputStream ins = new FileInputStream(file);
        ftpOperation.uploadToFtp(ins,fileName,false);

    }
}
