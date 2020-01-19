package com.youkeshu.hr.util;

import com.youkeshu.hr.config.SessionInterceptor;
import com.youkeshu.hr.entity.UserEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class WebUtils {

    /**
     * 直接获取当前线程的HttpServletRequest.
     *
     * @return javax.servlet.http.HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attributes)) {
            return ((ServletRequestAttributes) attributes).getRequest();
        } else {
            return null;
        }
    }

    public static UserEntity getSessionUser(){
        Object attribute = WebUtils.getRequest().getSession().getAttribute(SessionInterceptor.USER_AUTH);
        return (UserEntity) attribute;
    }

    public static String encryptedPassword(String pwd, String salt) {
        String s = DigestUtils.md5DigestAsHex("123456".getBytes());
        return s;
    }
}
