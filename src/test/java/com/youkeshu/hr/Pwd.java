package com.youkeshu.hr;

import org.junit.Test;
import org.springframework.util.DigestUtils;

/**
 * @Author: liuxing
 * @Date: 2020/1/17 17:31
 * @Description:
 */
public class Pwd {

    @Test
    public void pwd(){
        String s = DigestUtils.md5DigestAsHex("123456".getBytes());
        System.out.println(s);
    }

}