package com.hjj.dao;

import com.hjj.model.News;
import org.apache.ibatis.annotations.*;

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

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);
}
