package com.hjj.service;

import com.alibaba.fastjson.JSONObject;
import com.hjj.util.Util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/14.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "2EUWI7K4KJPNmjsYr07dmOA4CefntyFZuabLzKXL";
        String secretKey = "nwZRN6J_1jXF2deSTXFHG6pWUinfPqnIxQX4_0Vn";

        //String key=null;
        /* //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "/home/qiniu/test.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;*/
    /*
    上传图片
     */
    public String saveImage(MultipartFile file) throws IOException{
        try {
            String bucket = "news";
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!Util.isFileAllowed(fileExt)) {
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            logger.error("fileName:"+fileName);
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            //解析上传成功的结果
           // DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
           // System.out.println(putRet.key);
           // System.out.println(putRet.hash);
            logger.error("response",response.toString());
            if(response.isOK()&&response.isJson()){
                String key= JSONObject.parseObject(response.bodyString()).get("key").toString();
                return Util.QINIU_DOMAIN+key;
            }else return null;
        } catch (QiniuException ex) {
           // Response r = ex.response;
            logger.error("Exception:"+ex.getMessage());
            return null;
        }
    }


     /*
          上传视频
    */
    public String uploadVideo(MultipartFile file) throws IOException{
        try {
            String bucket = "video";
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken=auth.uploadToken(bucket);
            //得到上传文件的文件名的后缀名，并生成新的文件名
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!Util.isVideoFileAllowed(fileExt)) {
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            logger.error("fileName:"+fileName);
            //把文件转化为字节数组
            InputStream is = file.getInputStream();
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            byte[] b=new byte[1024];
            int len=-1;
            while((len=is.read(b))!=-1){
                bos.write(b,0,len);
            }
            byte[] uploadBytes=bos.toByteArray();
            System.out.println(uploadBytes.length);
            //进行上传操作，传入文件的字节数组，文件名，上传空间，得到回复对象
            Response response=uploadManager.put(file.getBytes(),fileName,upToken);
            if(response.isJson()&&response.isOK()){
                String key= JSONObject.parseObject(response.bodyString()).get("key").toString();
                return Util.QINIU_DOMAIN+key;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
}
