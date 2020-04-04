package com.gdy.boke.service;

import com.gdy.boke.model.ThemeInfo;

import java.util.List;

public interface ThemeService {
    public List<ThemeInfo> findAll();
    public void deleteById(Long id);
    ThemeInfo findByid(Long id);

}
