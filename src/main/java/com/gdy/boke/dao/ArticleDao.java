package com.gdy.boke.dao;

import com.gdy.boke.model.ArticleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ArticleDao extends JpaRepository<ArticleInfo,Long>,JpaSpecificationExecutor<ArticleInfo>{

    @Query(value = "select * from article_info a where a.create_time =  (select max(a1.create_time) from article_info a1 where a.create_user_id = a1.create_user_id)",nativeQuery = true)
    ArticleInfo findMostHot();



}
