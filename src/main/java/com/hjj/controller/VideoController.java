package com.hjj.controller;

import com.hjj.model.NewsType;
import com.hjj.service.QiniuService;
import com.hjj.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2017/8/22.
 */
@Controller
public class VideoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
    @Autowired
    QiniuService qiniuService;

    @RequestMapping(path={"/videoindex"},method = {RequestMethod.POST,RequestMethod.GET})
     public String video(Model model){
        model.addAttribute("newstype", NewsType.TYPE_VIDEO);
        return "video";
    }



    @RequestMapping(path={"/uploadVideo/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadVideo(@RequestParam("file") MultipartFile file){
        try{
            String fileUrl=qiniuService.uploadVideo(file);
            logger.error("fileUrl:"+fileUrl);
            if(null==fileUrl)
                return Util.getJSONString(1, "上传失败！");
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



}
