package com.gdy.boke.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity(name="article_info")
public class ArticleInfo implements Serializable{

    /**
     * 11
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="article_id")
    private Long id;
    private String articleTitle;
    private Long themeId;
    @Transient
    private String themeName;
    private String content;
    private Date createTime;
    private Date updateTime;
    private String createUser;
    private Long createUserId;
    private String imgSrc;
    @Transient
    private Integer commentCount;
    @Transient
    private List<CommentInfo> commentInfos;
    private Integer viewCount;
    private String articleSim;

}
