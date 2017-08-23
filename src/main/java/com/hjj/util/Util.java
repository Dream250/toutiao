package com.hjj.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/9.
 */
public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    /*
    图片上传
     */
    public static String QINIU_DOMAIN="http://ot5i0ccl4.bkt.clouddn.com/";
    public static String DOMAIN="http://127.0.0.1:8080/";
    //上传目录
    public static String IMAGE_DIR="D:/upload/";
    private static String[] IMAGE_FILE_EXT=new String[] {"png","bmp","jpg","jpeg"};

    //判断文件后缀名是否符合
    public static boolean isFileAllowed(String fileName){
        for(String ext : IMAGE_FILE_EXT){
            if(ext.equals(fileName))
                return true;
        }
        return false;
    }

    /*
    上传视频
     */
    public static String QINIU_DOMAIN2="http://ouvvok8ir.bkt.clouddn.com";
   // public static String DOMAIN="http://127.0.0.1:8080/";
    public static String VIDEO_DIR="D:/upload/";
    private static String[] VIDEO_FILE_EXT=new String[] {"mp4","rm","rmvb","avi"};

    public static boolean isVideoFileAllowed(String fileName){
        for(String ext : VIDEO_FILE_EXT)
            if(fileName.equals(ext))
                return true;
        return false;
    }

    //JSON串
    public static String getJSONString(int code){
        JSONObject json=new JSONObject();
        json.put("code",code);
        return json.toJSONString();
    }
    public static String getJSONString(int code,String msg){
        JSONObject json=new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json.toJSONString();
    }
    public static String getJSONString(int code,Map<String,Object> map){
        JSONObject json=new JSONObject();
        json.put("code",code);
        for(Map.Entry<String,Object> entry:map.entrySet()){
            json.put(entry.getKey(),entry.getValue());
        }
        return json.toJSONString();
    }


    //MD5加密
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }

}
