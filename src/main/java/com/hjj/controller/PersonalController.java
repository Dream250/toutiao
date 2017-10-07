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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */
@Controller
public class PersonalController {

    @Autowired
    HostHolder hostHolder;
    @Autowired
    NewsService newsService;
    @Autowired
    VideoService videoService;
    @Autowired
    LikeService likeService;
    @Autowired
    UserService userService;

    // localhost:8080/personal?userId=1
    @RequestMapping(path={"/personal"},method = {RequestMethod.GET,RequestMethod.POST})
    public String person(@RequestParam("userId") int userId,
                         Model model){
        int localUserId = hostHolder.getUser().getId();
        User user = userService.getUser(userId);
        Integer flag = 1;       // 是否是本地用户
        if(userId == localUserId)
                flag = 0;      //本地用户
        ViewObject vo = new ViewObject();
        vo.set("flag",flag);
        vo.set("user",user);
        model.addAttribute("vo",vo);

        //news
        List<ViewObject> vos1 = new ArrayList<ViewObject>();
        List<News> newsList = newsService.selectByUserIdAndOffset(userId,0,100);
        for(News news : newsList){
            ViewObject newsVo = new ViewObject();
            newsVo.set("news",news);
            newsVo.set("like", likeService.getLikeStatus(userId, EntityType.ENTITY_NEWS, news.getId()));
            vos1.add(newsVo);
        }
        model.addAttribute("vos1", vos1);

        //video
        //List<Video> videoList = videoService.getLatestNews(userId, offset, limit);
        List<Video> videoList = videoService.getLatestNews(userId,0, 100);
        List<ViewObject> vos2 = new ArrayList<ViewObject>();
        for(Video video : videoList){
            ViewObject videoVo = new ViewObject();
            videoVo.set("video",video);
            videoVo.set("like", likeService.getLikeStatus(userId, EntityType.ENTITY_VEDEO, video.getId()));
            vos2.add(videoVo);
        }
        model.addAttribute("vos2",vos2);
       /* List<News> news = newsService.selectByUserIdAndOffset(userId,0,100);
        vo.set("news",news);
        List<Video> video = videoService.getLatestNews(userId,0,100);
        vo.set("videos",video);
        model.addAttribute("vo",vo);*/
        return "personal";
    }

    @RequestMapping(path={"/delete/"},method = {RequestMethod.GET,RequestMethod.POST})
    public String delete(@RequestParam("newstype") int newstype,
                         @RequestParam("newsid") int newsid){
        try{
            if(newstype == 1){
                newsService.delete(newsid);
            }else if(newstype == 2){
                videoService.delete(newsid);
            }
            return "/personal?userId="+hostHolder.getUser().getId();
        }catch(Exception e){
            return Util.getJSONString(1, "删除失败！");
        }
    }
}
