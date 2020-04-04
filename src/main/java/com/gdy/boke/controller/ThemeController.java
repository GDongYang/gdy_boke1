package com.gdy.boke.controller;

import com.gdy.boke.constant.ResultData;
import com.gdy.boke.model.ThemeInfo;
import com.gdy.boke.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @RequestMapping("/theme/findAll")
    public void findAllThemes(){
        List<ThemeInfo> all = themeService.findAll();
    }

    @DeleteMapping("/theme/{themeId}")
    public ResultData deleteTheme(@PathVariable Long themeId){

        try {
            themeService.deleteById(themeId);
            return ResultData.success("删除主题成功");
        }catch (Exception e){
            return ResultData.fail("199999","删除失败");
        }


    }
}
