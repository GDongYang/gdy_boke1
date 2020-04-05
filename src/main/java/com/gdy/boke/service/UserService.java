package com.gdy.boke.service;

import com.gdy.boke.constant.ResultData;
import com.gdy.boke.model.UserInfo;

import java.util.Map;

public interface UserService {
    /**
     * 根据id查找user
     * @param id
     * @return
     */
    public UserInfo findById(Long id);

    public UserInfo userLogin(UserInfo userInfo);

    public UserInfo findByUserName(String userName);

    Map<String,Object> collectIndexData(Map paramMap);

    ResultData sendEmailCode(String email);

    ResultData registUser(String userName, String password, String realName, String userEmail, String userTel, String viryCode);
}
