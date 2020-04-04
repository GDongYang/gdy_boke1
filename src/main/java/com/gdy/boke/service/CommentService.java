package com.gdy.boke.service;

import com.gdy.boke.model.CommentInfo;

import java.util.List;

public interface CommentService {

    public List<CommentInfo> findByArticleId(Long articleId);

    public void create(CommentInfo commentInfo);

}


