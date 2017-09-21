package com.hjj.util;

/**
 * Created by Administrator on 2017/8/1.
 */
public class RedisKeyUtil {
    private static String SPIT=":";
    private static String BIZ_LIKE="LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_EVENT = "EVENT";

    //entityType:enentyId:like
    public static String getLikeKey(int entityType, int entityId){
        return String.valueOf(entityType)+SPIT+String.valueOf(entityId)+SPIT+BIZ_LIKE;
    }

    public static String getDisLikeKey(int entityId, int entityType){
        return String.valueOf(entityType)+SPIT+String.valueOf(entityId)+SPIT+BIZ_DISLIKE;
    }


    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

}
