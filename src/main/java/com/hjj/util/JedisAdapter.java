package com.hjj.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */
@Service
public class JedisAdapter implements InitializingBean{
    private static final Logger logger= LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool=null;

   /* public static void print(int index,Object obj){
        System.out.println(String.format("%d,%s", index, obj.toString()));
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        jedis.flushAll();

        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));
        jedis.setex("hello2", 15, "world");

        jedis.set("pv", "100");
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        jedis.decr("pv");
        print(3, jedis.get("pv"));

        jedis.incrBy("pv", 5);
        print(3, jedis.get("pv"));

        String listName = "listA";
        for (int i = 0; i < 10; i++) {
            jedis.rpush(listName, "a" + String.valueOf(i));
        }
        print(3, jedis.lrange(listName, 0, 10));
        print(4, jedis.llen(listName));
        print(5, jedis.lpop(listName));
        print(6, jedis.lindex(listName, 3));

        String userKey = "user12";
        jedis.hset(userKey, "name", "Jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "18812313213");
        jedis.hset(userKey, "school", "adfdf");

        print(12, jedis.hget(userKey, "name"));
        print(12, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "school");
        print(13, jedis.hgetAll(userKey));

        //set
        String set1 = "set1";
        String set2 = "set2";
        for (int i = 0; i < 10; ++i) {
            jedis.sadd(set1, String.valueOf(i));
            jedis.sadd(set2, String.valueOf(i * 2));
        }
        print(20, jedis.smembers(set1));
        print(21, jedis.smembers(set2));
        print(22, jedis.sinter(set1, set2));//交集
        print(22, jedis.sunion(set1, set2));//并集
        print(22, jedis.sdiff(set1, set2));
        jedis.srem(set1,"5");

        //优先队列
        String rankKey="rankKey";
        jedis.zadd(rankKey,15,"jim");
        jedis.zadd(rankKey,60,"ben");
        print(30,jedis.zcard(rankKey));
        print(31,jedis.zcount(rankKey,60,100));
        print(32,jedis.zscore(rankKey,"jim"));
        jedis.zincrby(rankKey,3,"jim");
        print(33,jedis.zscore(rankKey,"jim"));
        print(39,jedis.zrange(rankKey,1,1));
    }
*/
    @Override
    public void afterPropertiesSet() throws Exception {
        pool =new JedisPool("localhost",6379);
    }
    private Jedis getJedis(){
        return pool.getResource();
    }

    public long sadd(String key, String value){
        Jedis jedis=null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(null!=jedis){
                jedis.close();
            }
        }
    }

    public long srem(String key, String value){
        Jedis jedis=null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key, value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(null!=jedis){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value){
        Jedis jedis=null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if(null!=jedis){
                jedis.close();
            }
        }
    }

    public long scard(String key){
        Jedis jedis=null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(null!=jedis){
                jedis.close();
            }
        }
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key ,Object obj){
        setObject(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key,Class<T> clazz){
        String value=get(key);
        if(value!=null)
            return JSON.parseObject(value, clazz);
        return null;
    }

}
