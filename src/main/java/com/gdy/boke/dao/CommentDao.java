package com.gdy.boke.dao;

import com.gdy.boke.model.CommentInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentDao extends JpaRepository<CommentInfo,Long>{

    @Query(value = "SELECT  * FROM  Comment_Info WHERE Comment_Info .article_Id =?1 AND Comment_Info.to_user_id IS NULL order by  Comment_Info.create_Time asc",nativeQuery = true)
    List<CommentInfo> findByArticleId(@Param("articleId") Long articleId);

    @Query(value = "SELECT * FROM  Comment_Info WHERE  Comment_Info.to_Comment_Id =?1 AND Comment_Info.article_Id=?2 ORDER BY  Comment_Info.create_Time asc",nativeQuery = true)
    List<CommentInfo> findByParCommentId(@Param("parCommentId") Long parCommentId,@Param(value = "articleId")Long articleId);

}
