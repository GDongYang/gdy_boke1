package com.gdy.boke.service.impl;

import com.gdy.boke.dao.CommentDao;
import com.gdy.boke.model.CommentInfo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaseService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    private CommentDao commentDao;

    public  List<CommentInfo> findCommentsById(Long articleId){

        List<CommentInfo> parComments = commentDao.findByArticleId(articleId);
        //收集子评论
        if(parComments!=null){
            for(CommentInfo commentInfo : parComments){
                List<CommentInfo> sonComents = commentDao.findByParCommentId(commentInfo.getId(), commentInfo.getArticleId());
                log.info("get sonComments:"+sonComents);
                commentInfo.setSonComments(sonComents);
            }
        }
        return parComments;
    }
}
