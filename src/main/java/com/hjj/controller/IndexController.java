package com.hjj.controller;

import com.hjj.model.User;
import com.hjj.service.ToutiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.RedirectViewControllerRegistration;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Administrator on 2017/7/7.
 */
//@Controller
public class IndexController {
    @Autowired
    private ToutiaoService toutiaoService;

    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index(){
        return "hello! hahah<br>"+"service:"+toutiaoService.say();
    }

    //localhost:8080/profile/12/33?key=12&type=33
    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(
      @PathVariable("groupId") String groupId,
      @PathVariable("userId") int userId,
      @RequestParam(value="type",defaultValue = "1") int type,
      @RequestParam(value = "key",defaultValue="123") int key
    )
    {
       return String.format("groupId:{%s} \n userId:{%d} \n" +
               "type:{%d}\n key={%d}",groupId,userId,type,key);
    }

    @RequestMapping(value={"/vm"})
    public String news(Model model){
        model.addAttribute("value1","vv1");
        List<String> list= Arrays.asList(new String[]{"red","green","blue"});
        Map<String,String> map=new HashMap<String,String>();
        for(int i=0;i<4;++i){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("list",list);
        model.addAttribute("map",map);

        model.addAttribute("user",new User("hujj"));
        return "news";
    }

    @RequestMapping(value={"request"})
    @ResponseBody
    public String request(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ){
        StringBuilder sb=new StringBuilder();
        Enumeration<String> headNames=request.getHeaderNames();
        while(headNames.hasMoreElements()){
            String name=headNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }

        for(Cookie cookie : request.getCookies()){
            sb.append("####cookis:"+cookie.getName()+"<br>");
            sb.append("###"+cookie.getValue()+"<br>");
        }

        sb.append(request.getMethod());
        return sb.toString();
    }

    /*@RequestMapping(path={"response"})
    @ResponseBody
    public String response(

    ){

    }*/
    @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession session
    ){
        RedirectView red=new RedirectView("/",true);
        if(code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        session.setAttribute("index","safdas");
        return red;
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="key" ,required=false) String key){
        if("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("wrong key");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }
}
