package com.hjj.controller;

import com.hjj.model.*;
import com.hjj.service.NewsService;
import com.hjj.service.UserService;
import com.hjj.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    // localhost:8080/personal?userId=1
    @RequestMapping(path={"/personal"},method = {RequestMethod.GET,RequestMethod.POST})
    public String person(@RequestParam("userId") int userId,
                         Model model){
        int localUserId = hostHolder.getUser().getId();
        User user = hostHolder.getUser();
        Integer flag = 1;   // 是否是本地用户
        if(userId == localUserId)
                flag = 0;  //本地用户
        ViewObject vo = new ViewObject();
        vo.set("flag",flag);
        vo.set("user",user);

        List<News> news = newsService.selectByUserIdAndOffset(userId,0,100);
        vo.set("news",news);

        List<Video> video = videoService.getLatestNews(userId,0,100);
        vo.set("videos",video);

        model.addAttribute("vo",vo);
        return "personal";
    }
}
