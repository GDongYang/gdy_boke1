package com.gdy.boke.service.impl;

import com.gdy.boke.dao.UserDao;
import com.gdy.boke.model.UserInfo;
import com.gdy.boke.service.ArticleService;
import com.gdy.boke.service.ThemeService;
import com.gdy.boke.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
