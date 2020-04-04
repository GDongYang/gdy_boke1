package com.gdy.boke.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gdy.boke.constant.LoginException;
import com.gdy.boke.constant.UserInfoContext;
import com.gdy.boke.dao.ArticleDao;
import com.gdy.boke.dao.CommentDao;
import com.gdy.boke.model.ArticleInfo;
import com.gdy.boke.model.CommentInfo;
import com.gdy.boke.model.ThemeInfo;
import com.gdy.boke.model.UserInfo;
import com.gdy.boke.service.ArticleService;
import com.gdy.boke.service.DataCacheService;
import com.gdy.boke.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class ArticleServiceImpl extends BaseService implements ArticleService{

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private ThemeService themeService;

    @Autowired
    private DataCacheService dataCacheService;

    public String getToken(){
        //获取Token
        String token = dataCacheService.getToken();
        if(token==null||token.equals("")){
            //设置锁
            String requestId = UUID.randomUUID().toString();
            lock(requestId);
            try {
                
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                unlock(requestId);
            }
        }
        return  null;
    }

    private void unlock(String requestId) {
        dataCacheService.unlock(requestId);
    }

    private void lock(String requestId) {
       Long start = System.currentTimeMillis()+90000;
       while (!dataCacheService.setTokenLock(requestId)){
            //设置超时时间
           Long end = System.currentTimeMillis();
           if(end>start){
               throw new LoginException("获取锁超时");
           }
           try {
               Thread.sleep(500);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }

    @Override
    public ArticleInfo findById(Long id){
        ArticleInfo articleInfo = articleDao.findById(id).orElse(new ArticleInfo());
        //封装评论信息
        List<CommentInfo> comments = findCommentsById(id);
        articleInfo.setCommentInfos(comments);
        //评论数量
        articleInfo.setCommentCount(comments==null? 0 : comments.size());
        ThemeInfo themeInfo = themeService.findByid(articleInfo.getThemeId());
        //标题名称
        articleInfo.setThemeName(themeInfo.getThemeName());
        return  articleInfo;
    }

    @Override
    public Page<ArticleInfo> findAll(Map<String, Object> param, int page, int size) {
        Specification<ArticleInfo> specification = createSpecification(param);
        PageRequest pageRequest = PageRequest.of(page-1,size);
        Page<ArticleInfo> findPage = articleDao.findAll(specification,pageRequest);
        List<ArticleInfo> articles = findPage.getContent();
        for(ArticleInfo articleInfo : articles){
           articleInfo.setCommentInfos(commentDao.findByArticleId(articleInfo.getId()));
           articleInfo.setCommentCount(commentDao.findByArticleId(articleInfo.getId()).size());
        }
        return findPage;
    }

    @Override
    public ArticleInfo findMostHot() {
        ArticleInfo articleInfo = articleDao.findMostHot();
        articleInfo.setCommentInfos(findCommentsById(articleInfo.getId()));
        return articleInfo;
    }

    @Override
    public void update(ArticleInfo articleInfo) {
        articleDao.save(articleInfo);
    }

    public Long addArticle(Map paramMap){
        ArticleInfo article = new ArticleInfo();
        //封装文章信息
        article.setContent((String) paramMap.get("content"));
        article.setArticleTitle((String) paramMap.get("articleTitle"));
        article.setCreateTime(new Date());
        article.setCommentInfos(new ArrayList<>());
        article.setCommentCount(0);
        article.setViewCount(0);

        JSONObject userJson = UserInfoContext.get();
        if(userJson==null){
            throw new LoginException("清先登录再发布文章");
        }

        UserInfo userInfo = JSONObject.parseObject(userJson.toString(), UserInfo.class);
        //封装作者信息
        article.setCreateUser(userInfo.getUserName());
        article.setCreateUserId(userInfo.getUserId());
        ArticleInfo save = articleDao.save(article);
        return  save.getId();
    }



    @Override
    public ArticleInfo findByIdAndSetCount(Long articleId, String remoteAddr) {
        return findById(articleId);
    }

    /**
     * 动态条件构建
     */
    public Specification<ArticleInfo> createSpecification(Map<String,Object> findSearch){


        return new Specification<ArticleInfo>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<ArticleInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                //ID
                if(findSearch.get("id")!=null && !findSearch.get("id").equals("")){
                    predicateList.add(cb.like(root.get("id").as(String.class),"%"+(String)findSearch.get("id")+"%"));
                }

                //articleTitle
                if(findSearch.get("articleTitle")!=null && !findSearch.get("articleTitle").equals("")){
                    predicateList.add(cb.like(root.get("articleTitle").as(String.class),"%"+(String)findSearch.get("articleTitle")+"%"));
                }

                //createTime
                if(findSearch.get("createTime")!=null && !findSearch.get("createTime").equals("")){
                    predicateList.add(cb.like(root.get("createTime").as(String.class),"%"+(String)findSearch.get("createTime")+"%"));
                }

                //createUser
                if(findSearch.get("createUser")!=null && !findSearch.get("createUser").equals("")){
                    predicateList.add(cb.like(root.get("createUser").as(String.class),"%"+(String)findSearch.get("createUser")+"%"));
                }

                //themeId
                if(findSearch.get("themeId")!=null && !findSearch.get("themeId").equals("")){
                    predicateList.add(cb.equal(root.get("themeId").as(Long.class),(Long)findSearch.get("themeId")));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

}
