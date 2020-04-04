package com.gdy.boke.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "comment_info")
public class CommentInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private String userName;
    private Date createTime = new Date();
    private Long toUserId;
    private Long toCommentId;
    private String userImg;
    private String toUserName;
    @Transient
    private List<CommentInfo> sonComments;


}
