package com.hjj.controller;

import com.hjj.model.EntityType;
import com.hjj.model.HostHolder;
import com.hjj.model.News;
import com.hjj.model.ViewObject;
import com.hjj.service.LikeService;
import com.hjj.service.NewsService;
import com.hjj.service.ToutiaoService;
import com.hjj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */
@Controller
public class HomeController {
    @Autowired  //自动装配
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    ToutiaoService toutiaoService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/", "/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model,
        @RequestParam(value="pop",defaultValue = "0")int pop) {
        model.addAttribute("vos", getNews(0, 0, 10));
        model.addAttribute("pop",pop);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(@PathVariable("userId") int userId, Model model,
                            @RequestParam(value = "pop",defaultValue = "0") int pop) {
        model.addAttribute("vos", getNews(userId, 0, 10));
        model.addAttribute("pop",pop);
        return "home";
    }

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = toutiaoService.getLatestNews(userId, offset, limit);
        int localUserId = hostHolder.getUser() !=null?hostHolder.getUser().getId():0;
        List<ViewObject> vos = new ArrayList<>();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));

            if(localUserId!=0){
                vo.set("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
            }else{
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }

}
