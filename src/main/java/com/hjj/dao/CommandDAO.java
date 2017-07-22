package com.hjj.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Administrator on 2017/7/18.
 */
@Mapper
public interface CommandDAO {
    String TABLE_NAME="command";
    String INSERT_FIELDS="content,user_id,entity_id,entity_type," +
            "created_Date,status";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;
    @Insert({"insert into ",TABLE_NAME," (" ,INSERT_FIELDS,") values" })
    public void insert();
}
