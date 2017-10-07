package com.hjj.controller;

import com.hjj.model.*;
import com.hjj.service.LikeService;
import com.hjj.service.NewsService;
import com.hjj.service.UserService;
import com.hjj.service.VideoService;
import com.hjj.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/7.
 */
@Controller
public class SearchController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    NewsService newsService;
    @Autowired
    VideoService videoService;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;

    //搜索
    @RequestMapping(path = {"/search/{newsType}"},method ={RequestMethod.GET,RequestMethod.POST})
    public String searchNews (
            @PathVariable("newsType") int newsType,
            @RequestParam("keywords") String keywords) throws Exception{
        keywords=new String(keywords.getBytes("UTF-8"),"ISO-8859-1");
        String str="";
        if(newsType == 1) { //搜索news
            str="redirect:/pages/type1/?keywords="+keywords;
        }
        else if(newsType == 2) { //搜索video
            str="redirect:/pages/type2/?keywords="+keywords;
        }
        return str;
    }


    @RequestMapping(path={"/pages/type1/"})
    public String page(@RequestParam(value="keywords") String keywords,
                       @RequestParam(value = "page",defaultValue = "1") int page,
                       @RequestParam(value="pop",defaultValue = "0") int pop,
                       Model model) throws  Exception{
        //System.out.println("beforeadfasfsflsklfjslkfjslkjl;jl;j;l"+keywords);
        // keywords=new String(keywords.getBytes("ISO-8859-1"), "UTF-8");
        //System.out.println("adfasfsflsklfjslkfjslkjl;jl;j;l"+keywords);
        List<ViewObject> list=getNews(keywords,0,100);
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
        model.addAttribute("kw",keywords);
        return "home";
    }

    private List<ViewObject> getNews(String keywords, int offset, int limit) {
        List<News> newsList = newsService.selectByKeyWorlds(keywords, offset, limit);
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


    @RequestMapping(path = {"/pages/type2/"})
    public String page2(@RequestParam(value="keywords") String keywords,
                       @RequestParam(value = "page",defaultValue = "1") int page,
                       @RequestParam(value="pop",defaultValue = "0") int pop,
                       Model model) {
        List<ViewObject> list = getNews2(keywords, 0, 100);
        //总记录数
        int recordtotal = list.size();
        //每页的记录数
        int pagesize = 7;
        //总共页数
        int pagetotal = recordtotal / pagesize;
        if (recordtotal % pagesize != 0)
            pagetotal++;
        model.addAttribute("pagetotal", pagetotal);
        List<ViewObject> list2 = new ArrayList<>();
        for (int i = (page - 1) * pagesize; i < page * pagesize && i < recordtotal; i++)
            list2.add(list.get(i));
        model.addAttribute("vos", list2);
        model.addAttribute("cur_page", page);
        model.addAttribute("pop", pop);
        model.addAttribute("newstype", NewsType.TYPE_VIDEO);
        model.addAttribute("kw",keywords);
        return "video";
    }

    private List<ViewObject> getNews2(String keywords, int offset, int limit) {
        List<Video> videoList = videoService.selectByKeyWorlds(keywords, offset, limit);
        int localUserId = hostHolder.getUser() !=null?hostHolder.getUser().getId():0;
        List<ViewObject> vos = new ArrayList<>();
        for (Video video : videoList) {
            ViewObject vo = new ViewObject();
            vo.set("video", video);
            vo.set("user", userService.getUser(video.getUserId()));
            if(localUserId!=0){
                vo.set("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_VEDEO,video.getId()));
            }else{
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }
}
