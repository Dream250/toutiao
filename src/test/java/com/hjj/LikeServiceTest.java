package com.hjj;

/**
 * Created by Administrator on 2017/8/26.
 */

import com.hjj.service.LikeService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeServiceTest {

    @Autowired
    LikeService likeService;

    @Test
    public void testLike(){
        likeService.like(123,1,1);
        Assert.assertEquals(1,likeService.getLikeStatus(123,1,1));
        System.out.print("A");
    }

    @Test
    public void testLikeB(){
        System.out.print("B");
    }

    @Before("")
    public void setUp(){
        System.out.println("Setup");
    }
    @After("")
    public void tearDown(){

    }
    @BeforeClass
    public static void beforeClass(){

    }

    @AfterClass
    public static void afterClass(){

    }
}
