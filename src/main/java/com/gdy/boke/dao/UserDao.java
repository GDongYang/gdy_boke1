package com.gdy.boke.dao;

import com.gdy.boke.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserInfo,Long>{

    public UserInfo findByUserNameAndPassword(String userName,String password);

    public UserInfo findByUserName(String userName);

}
