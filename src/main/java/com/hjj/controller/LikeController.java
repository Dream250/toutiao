package com.hjj.controller;

import com.hjj.async.EventModel;
import com.hjj.async.EventProducer;
import com.hjj.async.EventType;
import com.hjj.model.*;
import com.hjj.service.LikeService;
import com.hjj.service.NewsService;
import com.hjj.service.VideoService;
import com.hjj.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        long likeCount=likeService.like(userId, NewsType.TYPE_NEWS,newsId);
        // 更新喜欢数
        News news = newsService.getById(newsId);
        int newsOwnerId = news.getUserId();
        newsService.updateLikeCount(newsId,(int) likeCount);

        if(userId != newsOwnerId){
            eventProducer.fireEvent(new EventModel(EventType.LIKE)
                    .setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
                    .setEntityType(NewsType.TYPE_NEWS).setEntityOwnerId(newsOwnerId));
        }
        return Util.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path={"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("newsId") int newsId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.dislike(userId, NewsType.TYPE_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int) likeCount);
        return Util.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path={"/like2"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like2(@RequestParam("videoId") int videoId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.like(userId, NewsType.TYPE_VIDEO,videoId);

        Video video = videoService.getVideoById(videoId);
        int videoOwnerId = video.getUserId();
        videoService.updateLikeCount(videoId,(int) likeCount);
        if(userId!=videoOwnerId) {
            eventProducer.fireEvent(new EventModel(EventType.LIKE)
                    .setActorId(hostHolder.getUser().getId()).setEntityId(videoId)
                    .setEntityType(NewsType.TYPE_VIDEO)
                    .setEntityOwnerId(videoOwnerId));
        }
        return Util.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path={"/dislike2"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike2(@RequestParam("videoId") int videoId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.dislike(userId, NewsType.TYPE_VIDEO,videoId);
        videoService.updateLikeCount(videoId,(int) likeCount);
        return Util.getJSONString(0,String.valueOf(likeCount));
    }

}
