package com.hjj.dao;

import com.hjj.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by Administrator on 2017/7/10.
 */
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME="login_ticket";
    String INSERT_FIELDS="user_id,ticket,expired,status";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values (#{userId},#{ticket}," +
            "#{expired},#{status})"})
    int addLoginTicket(LoginTicket ticket);

    @Select({"select ",SELECT_FIELDS, " from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectTicketByTicket(String ticket);

    @Update({"update ",TABLE_NAME,"set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,
                      @Param("status") int status);
}
