package com.hjj.model;

/**
 * Created by Administrator on 2017/7/25.
 */
public class EntityType {
    /*
    评论中心
     */
    public static int ENTITY_NEWS=1;
    public static int ENTITY_VEDEO=2;

    public static String get(int id){
        if(id == 1) return "news";
        if(id == 2) return "video";
        return null;
    }
}
