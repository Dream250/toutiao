package com.hjj.dao;

import com.hjj.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where status <> 1 and (from_id=#{userId} or to_id=#{userId}) order by id desc) tt group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where status <> 1 and (from_id=#{userId} or to_id=#{userId}) and from_id=#{systemId} order by id desc)  tt group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationSystemList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit,
                                      @Param("systemId") int systemId);

    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where status <> 1 and (from_id=#{userId} or to_id=#{userId}) and from_id<>#{systemId} order by id desc) tt group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationFriendList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit,
                                            @Param("systemId") int systemId);

    @Select({"select count(id) from ", TABLE_NAME, " where status <> 1 and has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnReadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);


    @Select({"select count(id) from ", TABLE_NAME, " where status <> 1 and has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId} and from_id=#{systemId}"})
    int getConversationSystemLetterUnReadCount(@Param("userId") int userId, @Param("conversationId") String conversationId,
                                               @Param("systemId") int systemId);

    @Select({"select count(id) from ", TABLE_NAME, " where status <> 1 and has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId} and from_id <> #{systemId}"})
    int getConversationFriendLetterUnReadCount(@Param("userId") int userId, @Param("conversationId") String conversationId,
                                               @Param("systemId") int systemId);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id=#{userId}"})
    int getConversationTotalCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where status = 0 and conversation_id=#{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    @Update({"update ",TABLE_NAME, "set status = #{status} where id = #{id}",})
    void updateStatus(@Param("id") int id,
                      @Param("status") int status);

    @Update({"update ",TABLE_NAME," set has_read = #{hasRead} where id = #{id}"})
    void updateHasRead(@Param("id") int id,
                       @Param("hasRead") int hasRead);
}
