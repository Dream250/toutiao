package com.hjj.dao;

import com.hjj.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by Administrator on 2017/7/7.
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME="user";
    String INSERT_FIELDS="name,password,salt,head_url,email";
    String SELECT_FIELDS="id,name,password,salt,head_url,email,authority";

    @Insert({"insert into",TABLE_NAME,"("+INSERT_FIELDS+")" +
            "values(#{name},#{password},#{salt},#{headUrl},#{email})"})
    int addUser(User user);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    User selectById(int id);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where name=#{name}"})
    User selectByName(String name);

    @Update({"update",TABLE_NAME,"set password=#{password} where id=#{id}"})
    boolean updatePassword(@Param("id") int id,
                        @Param("password") String password);

    @Select({"select authority from user where id = #{id}"})
    int selectAuthorityById(int id);
}
