package com.youkeshu.hr.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * @Author: liuxing
 * @Date: 2020/1/16 15:47
 * @Description:
 */
@Data
public class LoginUserDto {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

}