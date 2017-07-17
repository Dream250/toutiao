package com.hjj.controller;

import com.hjj.model.HostHolder;
import com.hjj.model.News;
import com.hjj.service.NewsService;
import com.hjj.service.QiniuService;
import com.hjj.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

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
    }


    /*@RequestMapping(path={"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try{
            String fileUrl= newsService.saveImage(file);
            if(null==fileUrl) {
                return Util.getJSONString(1,"上传图片失败！");
            }
            return Util.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("图片上传失败！"+e.getMessage());
            return Util.getJSONString(1,"上传图片失败！");
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
}
