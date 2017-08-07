package com.hjj.util;

/**
 * Created by Administrator on 2017/8/1.
 */
public class RedisKeyUtil {
    private static String SPIT=":";
    private static String BIZ_LIKE="LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_EVENT = "EVENT";

    public static String getLikeKey(int entityId, int entityType){
        return BIZ_LIKE+SPIT+String.valueOf(entityType)+SPIT+String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId, int entityType){
        return BIZ_DISLIKE+SPIT+String.valueOf(entityType)+SPIT+String.valueOf(entityId);
    }
    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

}
