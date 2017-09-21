package com.hjj.controller;

import com.hjj.model.*;
import com.hjj.service.*;
import com.hjj.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Controller
public class VideoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
    @Autowired
    QiniuService qiniuService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    VideoService videoService;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;
    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/videoindex"})
    public String video() {
        return "forward:/pages/type2?page=1";
    }

    @RequestMapping(path = {"/user/addVideo/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addVideo(@RequestParam("video") String video,
                           @RequestParam("title") String title,
                           @RequestParam("link") String link) {
        try {
            Video vi = new Video();
            if (hostHolder.getUser() != null) {
                vi.setUserId(hostHolder.getUser().getId());
            }
            vi.setVideo(video);
            vi.setTitle(title);
            vi.setLink(link);
            vi.setCreatedDate(new Date());
            videoService.addVideo(vi);
            return Util.getJSONString(0);   //发布成功！
        } catch (Exception e) {
            logger.error("e:" + e.getMessage());
            return Util.getJSONString(1, "发布失败！");
        }
    }

    @RequestMapping(path = {"/uploadVideo/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = qiniuService.uploadVideo(file);
            logger.error("fileUrl:" + fileUrl);
            if (null == fileUrl)
                return Util.getJSONString(1, "上传失败！");
            return Util.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("视频上传失败！" + e.getMessage());
            return Util.getJSONString(1, "视频上传失败！");
        }
    }

    @RequestMapping(path = {"/html"})
    public String h() {
        return "videodetail";
    }

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<Video> videoList = videoService.getLatestNews(userId, offset, limit);
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

    @RequestMapping(path = {"/pages/type2"})
    public String page(@RequestParam(value = "page") int page,
                       @RequestParam(value = "pop", defaultValue = "0") int pop,
                       Model model) {
        List<ViewObject> list = getNews(0, 0, 15);
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
        return "video";
    }


    @RequestMapping(path={"/video/{videoId}"}, method ={RequestMethod.GET})
    public String newsDetail(@PathVariable("videoId") int videoId,
                             Model model){
        try {
            Video video = videoService.getVideoById(videoId);

            if (null != video) {
                int localUserId = hostHolder.getUser() !=null?hostHolder.getUser().getId():0;
                if(localUserId!=0){
                    model.addAttribute("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_VEDEO,video.getId()));
                }else{
                    model.addAttribute("like",0);
                }
                List<Comment> comments = commentService.getCommentsByEntity(video.getId(), EntityType.ENTITY_VEDEO);
                List<ViewObject> commentVOs = new ArrayList<ViewObject>();
                logger.info("数量："+comments.size());
                for (Comment comment : comments) {
                    ViewObject vo = new ViewObject();
                    vo.set("comment", comment);
                    vo.set("user", userService.getUser(comment.getUserId()));
                    commentVOs.add(vo);
                }
                model.addAttribute("comments", commentVOs);
            }
            model.addAttribute("video", video);
            model.addAttribute("owner", userService.getUser(video.getUserId()));
            String url=video.getVideo();
            int index=url.lastIndexOf("?");
            url=url.substring(0,index);
            model.addAttribute("url",url);
        }catch (Exception e){
            logger.error("获取资讯明细错误" + e.getMessage());
        }
        return "videodetail";
    }


    @RequestMapping(path = {"/addComment2"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("videoId") int videoId,
                             @RequestParam("content") String content) {
        try{
            Comment comment=new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(videoId);
            comment.setEntityType(EntityType.ENTITY_VEDEO);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);
            //评论数量
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            videoService.updateCommentCount(comment.getEntityId(),count);
            //todo 异步
        }catch (Exception e){
            logger.error("错误" + e.getMessage());
        }
        return "redirect:/video/"+String.valueOf(videoId);
    }

}
