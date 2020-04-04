package com.gdy.boke.controller;

import com.gdy.boke.constant.LoginException;
import com.gdy.boke.constant.ResultData;
import com.gdy.boke.model.CommentInfo;
import com.gdy.boke.model.UserInfo;
import com.gdy.boke.service.ArticleService;
import com.gdy.boke.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController extends BaseController{

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;


    @PostMapping("/comment/testComment")
    @ResponseBody
    public ResultData testComment(@RequestBody  String content){

        System.out.println("评论方法进来了");
        return new ResultData();
    }

    @PostMapping("/comment/commentArticle")
    @ResponseBody
    public ResultData createComment(Long articleId, String commentContent, HttpServletRequest request){
        //判断用户是否登录
        UserInfo currentUser = getCurrentUser(request);
        if(currentUser==null){
            throw new LoginException("请先登录");
        }
        try {
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.setContent(commentContent);
            commentInfo.setArticleId(articleId);
            commentInfo.setUserId(currentUser.getUserId());
            commentInfo.setUserName(currentUser.getUserName());
            commentService.create(commentInfo);
            return new ResultData().success("评论成功");
        }catch (Exception e){
            return new ResultData().fail("19999","评论失败");
        }
    }

}
