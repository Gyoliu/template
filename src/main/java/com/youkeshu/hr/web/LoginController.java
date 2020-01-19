package com.youkeshu.hr.web;

import com.youkeshu.hr.config.SessionInterceptor;
import com.youkeshu.hr.dto.LoginUserDto;
import com.youkeshu.hr.dto.Result;
import com.youkeshu.hr.entity.UserEntity;
import com.youkeshu.hr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 15:42
 * @Description:
 */
@Controller
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Value("${password.encryption:none}")
    private String passwordType;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody @Validated LoginUserDto loginUserDto, HttpServletRequest request){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(loginUserDto.getUsername());
        userEntity.setPassword(loginUserDto.getPassword());
        if("md5".equalsIgnoreCase(passwordType)){
            String s = DigestUtils.md5DigestAsHex(loginUserDto.getPassword().getBytes());
            userEntity.setPassword(s);
        }
        userEntity.setStatus(1);
        Optional<UserEntity> one = userRepository.findOne(Example.of(userEntity));
        if(one.isPresent()){
            UserEntity userEntity1 = one.get();
            request.getSession().setAttribute(SessionInterceptor.USER_AUTH, userEntity1);
            request.getSession().setAttribute("username", userEntity1.getUsername());
            return new Result(userEntity1);
        }
        return new Result(403, "用户或密码错误");
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute(SessionInterceptor.USER_AUTH);
        response.sendRedirect("/login");
    }
}