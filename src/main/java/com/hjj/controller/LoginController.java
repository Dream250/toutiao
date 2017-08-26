package com.hjj.controller;


import com.hjj.async.EventModel;
import com.hjj.async.EventProducer;
import com.hjj.async.EventType;
import com.hjj.service.UserService;
import com.hjj.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/9.
 */
@Controller
public class LoginController {
    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path={"/logout/"}, method={RequestMethod.GET,RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }


    @RequestMapping(path={"/login/"}, method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember",defaultValue = "0") int rememberme,
                        HttpServletResponse response){
        try{
            Map<String,Object> map=userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

                String email=(String)map.get("email");
                eventProducer.fireEvent(new
                        EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
                        .setExt("username", username).setExt("email", email));

                return Util.getJSONString(0, "成功");
            }else{
                return Util.getJSONString(1, map);
            }
        }catch (Exception e){
            logger.error("异常" + e.getMessage());
            return Util.getJSONString(1, "异常");
        }
    }

    //http://localhost:8080/reg/?username=123&password=123123
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email,
                           @RequestParam(value="rember", defaultValue = "0") int rememberme,
                        HttpServletResponse response
    ){
        try{
            Map<String,Object> map=userService.register(username,password,email);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/"); //路径全站有效
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

                eventProducer.fireEvent(new
                        EventModel(EventType.Register).setActorId((int) map.get("userId"))
                        .setExt("username", username).setExt("email", email));
                return Util.getJSONString(0,"注册成功");
            }else return Util.getJSONString(1,map);
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return Util.getJSONString(1,"注册异常");
        }
    }
}
