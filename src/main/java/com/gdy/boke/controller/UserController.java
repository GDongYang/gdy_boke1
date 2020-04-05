package com.gdy.boke.controller;

import com.gdy.boke.constant.ResultData;
import com.gdy.boke.model.UserInfo;
import com.gdy.boke.service.ArticleService;
import com.gdy.boke.service.ThemeService;
import com.gdy.boke.service.UserService;
import com.gdy.boke.util.CacheUtil;
import com.gdy.boke.util.CookieUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;


@Api(description = "用户中心接口")
@Controller
public class UserController extends BaseController{


    @Autowired
    private UserService userService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private ArticleService articleService;


    @RequestMapping("/")
    public ModelAndView toLogin()   {
        ModelAndView model = new ModelAndView("login");
        return model;
    }

    /**
     * 博客首页
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView toIndex(HttpServletRequest request,Map paramMap){
        ModelAndView md = new ModelAndView("index");
        //封装首页所需的所有数据
        Map<String,Object> map = userService.collectIndexData(paramMap);
        //查询用户是否登录
        UserInfo currentUser = getCurrentUser(request);
        map.put("userInfo",currentUser);
        md.addObject("data",map);
        return md;
    }

    @GetMapping(value = "/toLogin")
    public String comeLogin(){
        return "login";
    }

    /**
     * 用户注销
     * @param userId
     * @param session
     * @return
     */
    @GetMapping("/user/logout/{userId}")
    public void userLogout(@PathVariable("userId") String userId, HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException {
        //用户注销删除cookie
        String name = CookieUtil.deleteLoginToken(request, response);
        //删除缓存的user信息
        cacheUtil.deleteCatch(name);
        response.sendRedirect("/index");
    }

    @PostMapping(value = "/toLogin")
    public ModelAndView toLogin(UserInfo user, HttpSession session,HttpServletResponse response)  {
        UserInfo findUser = userService.userLogin(user);
        ModelAndView md = new ModelAndView();
        if(findUser==null){
            ResultData rd = ResultData.fail("19999", "用户名不存在或密码错误");
            md.setViewName("/login");
            md.addObject("resultData",rd);
            return md;
        }
        //写入cookie
        CookieUtil.writeLoginToken(response,session.getId());
        //将用户信息写入redis缓存
        cacheUtil.catchObject(session.getId(),findUser);
        //返回MV
        md.setViewName("redirect:index");
        return md;
    }

    /**
     * 用户注册发送验证码接口
     */
    @ApiOperation(value = "用户注册发送验证码", notes="用户注册发送验证码",httpMethod = "POST")
    @RequestMapping(value = "/user/sendEmailCode",method = RequestMethod.POST)
    @ResponseBody
    public ResultData sendEmailCode(@ApiParam(name = "email",required = true,value = "用户邮箱") @RequestParam("email") String email){
        return  userService.sendEmailCode(email);
    }

    /**
     * 用户注册
     */
    @ApiOperation(value = "用户注册", notes="用户注册",httpMethod = "POST")
    @RequestMapping(value = "/user/registUser",method = RequestMethod.POST)
    @ResponseBody
    public ResultData registUser(
            @ApiParam(name = "userName",required = true,value = "用户名")
            @RequestParam("userName") String userName,

            @ApiParam(name = "password",required = true,value = "密码")
            @RequestParam("password") String password,

            @ApiParam(name = "realName",required = true,value = "真实姓名")
            @RequestParam("realName") String realName,

            @ApiParam(name = "userEmail",required = true,value = "邮箱")
            @RequestParam("userEmail") String userEmail,

            @ApiParam(name = "userTel",required = true,value = "用户电话")
            @RequestParam("userTel") String userTel,

            @ApiParam(name = "viryCode",required = true,value = "验证码")
            @RequestParam("viryCode") String viryCode
    ){
        return  userService.registUser(userName,password,realName,userEmail,userTel,viryCode);
    }

}
