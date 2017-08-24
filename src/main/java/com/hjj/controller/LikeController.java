package com.hjj.controller;

import com.hjj.async.EventModel;
import com.hjj.async.EventProducer;
import com.hjj.async.EventType;
import com.hjj.model.EntityType;
import com.hjj.model.HostHolder;
import com.hjj.model.News;
import com.hjj.service.LikeService;
import com.hjj.service.NewsService;
import com.hjj.service.VideoService;
import com.hjj.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/8/1.
 */
@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    VideoService videoService;

    @RequestMapping(path={"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.like(userId, EntityType.ENTITY_NEWS,newsId);

        // 更新喜欢数
        News news = newsService.getById(newsId);

        newsService.updateLikeCount(newsId,(int) likeCount);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId));

        return Util.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path={"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("newsId") int newsId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.dislike(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int) likeCount);
        return Util.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path={"/like2"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like2(@RequestParam("videoId") int videoId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.like(userId, EntityType.ENTITY_VEDEO,videoId);

        videoService.updateLikeCount(videoId,(int) likeCount);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(videoId));


        return Util.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path={"/dislike2"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike2(@RequestParam("videoId") int videoId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.dislike(userId, EntityType.ENTITY_VEDEO,videoId);
        videoService.updateLikeCount(videoId,(int) likeCount);
        return Util.getJSONString(0,String.valueOf(likeCount));
    }

}
