package com.hjj;

import com.hjj.dao.LoginTicketDAO;
import com.hjj.dao.NewsDAO;
import com.hjj.dao.UserDAO;
import com.hjj.model.LoginTicket;
import com.hjj.model.News;
import com.hjj.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class InitDatebaseTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;
	@Test
	public void contextLoads() {
        Random rand=new Random();
	    for(int i=0;i<5;++i){
            User user=new User();
            user.setHeadUrl(String .format("http://images.nowcoder.com/head/%dt.png",rand.nextInt(100)));
            user.setName(String.format("User%d",i));
            user.setSalt("");
            user.setPassword("");
            userDAO.addUser(user);

            News news=new News();
            news.setCommentCount(i);
            Date date=new Date();
            //DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date.setTime(date.getTime()+1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String .format("http://images.nowcoder.com/head/%dm.png",rand.nextInt(100)));
            news.setLikeCount(i+1);
            news.setUserId(i);
            news.setTitle("title");
            news.setLink("http://www.baidu.com");
            newsDAO.addNews(news);


            LoginTicket t=new LoginTicket();
            t.setStatus(0);
            t.setUserId(i);
            t.setExpired(date);
            t.setTicket(String.format("afdasfs%d", i));
            loginTicketDAO.addLoginTicket(t);
            loginTicketDAO.updateStatus(t.getTicket(),3);
        }

        //Assert.assertEquals(1, loginTicketDAO.selectTicketByTicket("afdasfs87").getUserId());
        Assert.assertEquals(3, loginTicketDAO.selectTicketByTicket("afdasfs1").getStatus());
    }
}
