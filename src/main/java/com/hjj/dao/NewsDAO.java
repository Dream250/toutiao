package com.hjj.dao;

import com.hjj.model.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME="news";
    String INSERT_FIELDS="title,link,image,like_count,comment_count,created_date," +
            "user_id";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values (" +
            "#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    void addNews(News news);

    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    News selectById(int id);


    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);


}
