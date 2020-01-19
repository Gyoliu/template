package com.youkeshu.hr.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 15:03
 * @Description:
 */
@Controller
public class Helloworld {

    @ResponseBody
    @RequestMapping("/hello")
    public Map hello(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", "1");
        map.put("code", "1");
        return map;
    }

    @RequestMapping("/helloPage")
    public String hello1(){
        return "hello";
    }

}