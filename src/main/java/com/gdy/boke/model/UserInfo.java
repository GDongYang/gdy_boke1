package com.gdy.boke.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="user_info")
@Data
public class UserInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    private String userName;
    private String password;
    private Integer userRole;
    private String realName;
    private String userEmail;
    private String userTel;
    /**
     * 用户头像
     */
    private String 	headSculpture;
}
