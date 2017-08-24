package com.hjj.service;

import com.hjj.dao.NewsDAO;
import com.hjj.model.News;
import com.hjj.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/8.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

   /* public List<News> getLatestNews(
            int userId,int offset,int limit
    ){
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }*/
    /*本地保存测试
    public String saveImage(MultipartFile file) throws IOException {
        int index=file.getOriginalFilename().lastIndexOf(".");
        if(index<0) return null;
        String fileExt = file.getOriginalFilename().substring(index+1).toLowerCase();
        if(!Util.isFileAllowed(fileExt)) return null;

        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        Files.copy(file.getInputStream(),new File(Util.IMAGE_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return Util.DOMAIN + "image?name=" + fileName;
    }

    //本地保存测试
    public String saveVideo(MultipartFile file) throws IOException {
        String name=file.getOriginalFilename();
        int index=name.lastIndexOf(".");
        if(index<0) return null;
        String ext=name.substring(index+1);
        if(!Util.isVideoFileAllowed(ext)) return null;

        String fileName=UUID.randomUUID().toString().replaceAll("-","")+"."+ext;
        Files.copy(file.getInputStream(),new File(Util.VIDEO_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return Util.DOMAIN+"video?name="+fileName;
    }*/

    public  void addNews(News news){
         newsDAO.addNews(news);
    }

    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

    public News selectById(int newsId){
        return newsDAO.selectById(newsId);
    }

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }

    public int updateLikeCount(int id, int count) {
        return newsDAO.updateLikeCount(id, count);
    }
}
