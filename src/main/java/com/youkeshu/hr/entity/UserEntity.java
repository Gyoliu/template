package com.youkeshu.hr.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 15:51
 * @Description:
 */
@Data
@Entity
@Table(name = "hr_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private Date createDate;

    /**
     * 0 禁用 1启用
     */
    @Column(name = "status")
    private Integer status;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

}