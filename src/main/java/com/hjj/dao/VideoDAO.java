package com.hjj.dao;

import com.hjj.model.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Mapper
public interface VideoDAO {
    String TABLE_NAME="video";
    String INSERT_FIELDS="title,content,video,like_count,comment_count,created_date," +
            "user_id";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values (" +
            "#{title},#{content},#{video},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    void addVideo(Video video);

   /* @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    Video selectById(int id);*/
    @Update({"update ",TABLE_NAME," set status = #{status} where id=#{id}"})
    void updateVideoStatus(@Param("status") int status,
                           @Param("id") int id);

    List<Video> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit,@Param("status") int status);

    List<Video> selectByKeyWorlds(@Param("keywords") String keywords,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit,
                                  @Param("status") int status);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    Video getById(int id);

}
