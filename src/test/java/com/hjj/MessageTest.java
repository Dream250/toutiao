package com.hjj;

/**
 * Created by Administrator on 2017/8/26.
 */

import com.hjj.model.Message;
import com.hjj.model.News;
import com.hjj.service.MessageService;
import com.hjj.service.NewsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class MessageTest {
    /*@Autowired
    MessageService messageService;

    @Test
    public void testMessasge(){
        List<Message> messageList= messageService.getConversationList(1,0,10);

        System.out.println("messageList.size :"+messageList.size());
        Assert.assertNotNull(messageList);
        *//*likeService.like(123,1,1);
        Assert.assertEquals(1,likeService.getLikeStatus(123,1,1));
        System.out.print("A");*//*
    }*/
    @Autowired
    NewsService newsService;

    @Test
    public void test(){
        List<News> list=newsService.selectByKeyWorlds("图",0,100);
        System.out.println("list.size()++++++++++++"+list.size());
    }
}
