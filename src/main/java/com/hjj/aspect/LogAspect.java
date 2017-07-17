package com.hjj.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/7.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.hjj.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb=new StringBuilder();
        for(Object arg:joinPoint.getArgs()){
            sb.append("arg:"+arg.toString()+"|");
        }
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("before time:" + df.format(new Date()));
        logger.info("beforeMethod!"+sb.toString());
    }

    @After("execution(* com.hjj.controller.*Controller.*(..))")
    public void afterMethod(){
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("after time:"+df.format(new Date()));
        logger.info("afterMethod!");
    }
}
