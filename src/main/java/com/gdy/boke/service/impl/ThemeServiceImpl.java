package com.gdy.boke.service.impl;

import com.gdy.boke.dao.ThemeDao;
import com.gdy.boke.model.ThemeInfo;
import com.gdy.boke.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService{

    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private RedisTemplate redisTemplate;

    public List<ThemeInfo> findAll(){
        List<ThemeInfo> redisThemes = null;
        redisThemes = (List<ThemeInfo>) redisTemplate.opsForValue().get("themes");
        if(redisThemes==null){
            redisThemes = themeDao.findAll();
            redisTemplate.opsForValue().set("themes",redisThemes);
        }
        return redisThemes;
    }

    public void deleteById(Long id){
        themeDao.deleteById(id);
        //清空redis中的数据
        redisTemplate.delete("themes");
    }

    @Override
    public ThemeInfo findByid(Long id) {
        return themeDao.findById(id).orElse(new ThemeInfo());
    }

}
