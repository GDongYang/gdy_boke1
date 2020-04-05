package com.gdy.boke.service.impl;

import com.gdy.boke.constant.Constant;
import com.gdy.boke.constant.RedisKeyConstant;
import com.gdy.boke.constant.ResultData;
import com.gdy.boke.dao.UserDao;
import com.gdy.boke.model.UserInfo;
import com.gdy.boke.service.ArticleService;
import com.gdy.boke.service.ThemeService;
import com.gdy.boke.service.UserService;
import com.gdy.boke.util.SendMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    public UserInfo findById(Long userId){
        UserInfo userInfo = userDao.findById(userId).orElse(new UserInfo());
        return userInfo;
    }

    public UserInfo userLogin(UserInfo userInfo){
        return userDao.findByUserNameAndPassword(userInfo.getUserName(),userInfo.getPassword());
    }

    @Override
    public UserInfo findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public Map<String, Object> collectIndexData(Map paramMap) {
        Map<String,Object> map = new HashMap<>();
        map.put("themes",themeService.findAll());
        map.put("articles",articleService.findAll(new HashMap<>(),1,5));
        return map;
    }

    @Override
    public ResultData sendEmailCode(String userEmail) {

        //查询该email是否已经存在
        UserInfo byUserEmail = userDao.findByUserEmail(userEmail);
        if(byUserEmail!=null){
            return ResultData.fail("50000","该邮箱已经被注册，换个邮箱试试");
        }
        //新邮箱。发送验证码
        boolean result = sendMsgUtil.sendEmailCode(userEmail);
        if(result==false){
            return ResultData.fail("50001","邮箱验证码发送失败，请稍后重试");
        }
        return ResultData.success("验证码已发送，请注意查收");
    }

    @Override
    public ResultData registUser(String userName, String password, String realName, String userEmail, String userTel, String viryCode) {
        //校验验证码是否正确
        String code = (String)redisTemplate.opsForValue().get(RedisKeyConstant.REDIS_KEY_EMAIL + userEmail);
        if(!code.equals(viryCode)){
            return ResultData.fail("50000","验证码错误，请一分钟后重试");
        }
        //创建对象
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setPassword(password);
        userInfo.setRealName(realName);
        userInfo.setUserEmail(userEmail);
        userInfo.setUserTel(userTel);
        userInfo.setUserRole(Constant.MOREN_USER_ROLE);
        userDao.save(userInfo);
        return ResultData.success("用户创建成功");
    }
}
