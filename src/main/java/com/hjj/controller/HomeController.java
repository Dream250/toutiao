package com.hjj.controller;


import com.hjj.model.*;
import com.hjj.service.LikeService;
import com.hjj.service.NewsService;
import com.hjj.service.ToutiaoService;
import com.hjj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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

    /*@RequestMapping(path = {"/", "/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model,
        @RequestParam(value="pop",defaultValue = "0")int pop) {
        List<ViewObject> list=getNews(0,0,15);
        int recordtotal=list.size();
        int pagesize=5;
        int pagetotal=recordtotal/pagesize+recordtotal%pagesize;
        model.addAttribute("vos", getNews(0, 0,15));
        model.addAttribute("pop",pop);
        return "home";
    }*/

    //  /pages/type1?page=1
    @RequestMapping(path={"/pages/type1"})
    public String page(@RequestParam(value="page") int page,
                       @RequestParam(value="pop",defaultValue = "0") int pop,
                       Model model){
        List<ViewObject> list=getNews(0,0,15);
        //总记录数
        int recordtotal=list.size();
        //每页的记录数
        int pagesize=7;
        //总共页数
        int pagetotal=recordtotal/pagesize;
        if(recordtotal%pagesize!=0)
            pagetotal++;
        model.addAttribute("pagetotal",pagetotal);
        List<ViewObject> list2=new ArrayList<>();
        for(int i=(page-1)*pagesize;i<page*pagesize&&i<recordtotal;i++)
            list2.add(list.get(i));
        model.addAttribute("vos", list2);
        model.addAttribute("pop",pop);
        model.addAttribute("cur_page",page);

        model.addAttribute("newstype", NewsType.TYPE_NEWS);
        return "home";
    }

    @RequestMapping(path={"/","/index"})
    public String index(){
        return "forward:/pages/type1?page=1";
    }

    @RequestMapping(path = {"/user/{userId}/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(@PathVariable("userId") int userId,
                            Model model,
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
