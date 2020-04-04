package com.gdy.boke.aop;

import com.gdy.boke.model.ArticleInfo;
import com.gdy.boke.service.ArticleService;
import com.gdy.boke.util.CacheUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 文章阅读量记录
 */
@Aspect
@Component
@Order(2)
public class ViewCountAdvice {

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private ArticleService articleService;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ViewCountAdvice.class);

    @Pointcut("execution(public * com.gdy.boke.service.ArticleService.findByIdAndSetCount(..))")
    public void recordViewCount(){

    }

    @Before("recordViewCount()")
    public void doBefore(JoinPoint joinPoint){
        Long id = (Long) joinPoint.getArgs()[0];
        String host = (String)joinPoint.getArgs()[1];
        System.out.println("get host addr"+host);
        //根据主机名和文章id生成redisKey
        String redisKey = generateRediskey(joinPoint);
        //查看缓存是否存在
        String result = (String)cacheUtil.getCatch(redisKey);
        if(result==null){
            //该文章阅读量+1
            ArticleInfo articleInfo = articleService.findById(id);
            logger.info("start articleInfo viewCount:"+articleInfo.getViewCount());
            articleInfo.setViewCount(articleInfo.getViewCount()+1);
            articleService.update(articleInfo);
            ArticleInfo after = articleService.findById(id);
            logger.info("after articleInfo viewCount:"+articleInfo.getViewCount());
            //添加缓存已阅读
            cacheUtil.catchObject(redisKey,30*60L,"1");
        }else {
            //最近已经阅读过
        }
    }

    public String generateRediskey(JoinPoint joinPoint){
        Long articleId = (Long) joinPoint.getArgs()[0];
        String host = (String)joinPoint.getArgs()[1];
        return host+"articleId:"+articleId;
    }
}
