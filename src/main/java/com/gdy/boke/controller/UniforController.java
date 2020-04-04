package com.gdy.boke.controller;

import com.gdy.boke.model.ThemeInfo;
import com.gdy.boke.model.UniformData;
import com.gdy.boke.model.UserInfo;
import com.gdy.boke.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UniforController extends BaseController{

    @Autowired
    private ThemeService themeService;

    @RequestMapping("/uniformImf")
    @ResponseBody
    public UniformData getUniform(HttpServletRequest request){

        UniformData uniformData = new UniformData();
        //查询用户是否登录
        UserInfo currentUser = getCurrentUser(request);
        //查询所有的主题类别
        List<ThemeInfo> themes = themeService.findAll();

        if(currentUser!=null){
            uniformData.setUserInfo(currentUser);
        }
        uniformData.setThemes(themes);
        return uniformData;
    }
}
