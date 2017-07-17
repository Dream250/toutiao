package com.hjj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/7/7.
 */
@Controller
public class SettingController {
    @RequestMapping("/setting")
    @ResponseBody
    public String set(){
        return "this is a setting!";
    }
}
