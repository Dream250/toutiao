package com.hjj.service;

import com.hjj.dao.NewsDAO;
import com.hjj.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */
@Service
public class ToutiaoService {
    public String say(){
        return "this is a service";
    }
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(
            int userId,int offset,int limit
    ){
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }
}
