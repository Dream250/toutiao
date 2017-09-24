package com.hjj.controller;

import com.hjj.model.User;
import com.hjj.service.UserService;
import com.hjj.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/9/24.
 */
@Controller
public class ModifyController {
    private static final Logger logger = LoggerFactory.getLogger(ModifyController.class);

    @Autowired
    UserService userService;
    @RequestMapping(path={"/modify"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    /**
     * username 用户名
     * password  用户的原密码
     * password2  用户的修改密码
     */
    public String modifyPassword(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("password1") String password1){
        try{
            User user=userService.getUserByName(username);
            String salt=user.getSalt();
            String pwd=user.getPassword();
            if(!Util.MD5(password+salt).equals(pwd)){
                //logger.error("原密码有误！");
                return Util.getJSONString(1,"原密码有误！");
            }
            password1=Util.MD5(password1+salt);
            //logger.error("password1"+password1);
            userService.modifyPassword(user.getId(),password1);
            return Util.getJSONString(0,"修改成功");
        }catch (Exception e) {
            logger.error("修改异常"+e.getMessage());
            return Util.getJSONString(1,"修改异常，请重新修改");
        }
    }
}
