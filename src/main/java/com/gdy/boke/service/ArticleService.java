package com.gdy.boke.service;

import com.gdy.boke.model.ArticleInfo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ArticleService {

    public ArticleInfo findById(Long id);

    public Page<ArticleInfo> findAll(Map<String,Object> param,int page,int size);

    public ArticleInfo findMostHot();

    void update(ArticleInfo articleInfo);

    Long addArticle(Map paramMap);


    ArticleInfo findByIdAndSetCount(Long articleId, String remoteAddr);
}
