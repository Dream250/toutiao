package com.hjj.service;

import com.hjj.dao.VideoDAO;
import com.hjj.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */
@Service
public class VideoService {
    @Autowired
    VideoDAO videoDAO;

    public void addVideo(Video video){
        videoDAO.addVideo(video);
    }

    public Video getVideoById(int id){
         return videoDAO.getById(id);
    }

    public void updateCommentCount(int id,int commentCount){
        videoDAO.updateCommentCount(id,commentCount);
    }

    public void updateLikeCount(int id,int likeComment){
        videoDAO.updateLikeCount(id,likeComment);
    }

    public List<Video> getLatestNews(
            int userId,int offset,int limit
    ){
        return videoDAO.selectByUserIdAndOffset(userId,offset,limit,0);//选择未被删除的，status=0
    }

    public void delete(int id){
        videoDAO.updateVideoStatus(1,id); //1代表已删除
    }

    //根据关键字匹配 title or content 查询 news
    public List<Video> selectByKeyWorlds(String keywords,int offset, int limit){
        keywords="%"+keywords+"%";
        return videoDAO.selectByKeyWorlds(keywords,offset,limit,0);
    }
}
