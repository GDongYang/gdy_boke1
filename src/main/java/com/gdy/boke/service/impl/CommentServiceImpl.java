package com.gdy.boke.service.impl;

import com.gdy.boke.dao.CommentDao;
import com.gdy.boke.model.CommentInfo;
import com.gdy.boke.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    public List<CommentInfo> findByArticleId(Long articleId){

        return commentDao.findByArticleId(articleId);
    }

    @Override
    public void create(CommentInfo commentInfo) {
        commentDao.save(commentInfo);
    }

}
