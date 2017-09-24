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
public class AdministratorController {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorController.class);
    @Autowired
    UserService userService;

    @RequestMapping(path={"/reset"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String resetUserPassword(@RequestParam("username") String username,
                                  @RequestParam("password") String password){
        try{
            User user = userService.getUserByName(username);
            if(null==user){
                return Util.getJSONString(1,"用户名不存在！");
            }
            int id = user.getId();
            String salt=user.getSalt();
            userService.modifyPassword(id,Util.MD5(password+salt));
            return Util.getJSONString(0,"修改成功！");
        }catch (Exception e){
            logger.error("修改异常！"+e.getMessage());
            return Util.getJSONString(1,"修改异常，请重新修改！");
        }
    }
}
