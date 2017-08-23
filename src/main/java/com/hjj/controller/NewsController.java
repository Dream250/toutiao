package com.hjj.controller;

import com.hjj.model.*;
import com.hjj.service.*;
import com.hjj.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;
    @Autowired
    QiniuService qinniuService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;

    @RequestMapping(path={"/news/{newsId}"}, method ={RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId,
                             Model model){
        try {
            News news = newsService.selectById(newsId);


            if (null != news) {
                int localUserId = hostHolder.getUser() !=null?hostHolder.getUser().getId():0;
                if(localUserId!=0){
                    model.addAttribute("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
                }else{
                    model.addAttribute("like",0);
                }


                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
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
            model.addAttribute("news", news);
            model.addAttribute("owner", userService.getUser(news.getUserId()));
        }catch (Exception e){
        logger.error("获取资讯明细错误" + e.getMessage());
       }
        return "detail";
    }

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {
        try{
            Comment comment=new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);
            //评论数量
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(),count);
            //todo 异步
        }catch (Exception e){
            logger.error("错误" + e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }

    /*本地测试
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(Util.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }*/


    @RequestMapping(path={"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try{
            String fileUrl= qinniuService.saveImage(file);
            logger.error("fileUrl:"+fileUrl);
            if(null==fileUrl) {
                return Util.getJSONString(1,"上传图片失败！");
            }
            return Util.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("图片上传失败！"+e.getMessage());
            return Util.getJSONString(1,"上传图片失败！");
        }
    }

    @RequestMapping(path = {"/user/addNews/"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try{
            News news=new News();
            if(hostHolder.getUser() !=null){
                news.setUserId(hostHolder.getUser().getId());
            }else {
                //匿名
                news.setUserId(123);
            }
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            newsService.addNews(news);
            return Util.getJSONString(0);
        }catch (Exception e){
            return Util.getJSONString(1,"发布失败！");
        }
    }

   /* @RequestMapping(path={"/uploadV/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadVideo(@RequestParam("file") MultipartFile file){
        try{
            String fileUrl=qinniuService.uploadVideo(file);
            logger.error("fileUrl:"+fileUrl);
            if(null==fileUrl)
                return Util.getJSONString(1,"上传失败！");
            return Util.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("视频上传失败！"+e.getMessage());
            return Util.getJSONString(1,"视频上传失败！");
        }
    }

    @RequestMapping(path={"/html"})
    public String h(){
        return "home2";
    }
*/

}
