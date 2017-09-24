package com.hjj.async.handler;

import com.hjj.async.EventHandler;
import com.hjj.async.EventModel;
import com.hjj.async.EventType;
import com.hjj.model.EntityType;
import com.hjj.model.HostHolder;
import com.hjj.model.Message;
import com.hjj.model.User;
import com.hjj.service.MessageService;
import com.hjj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */
@Component
public class LikeHandler implements EventHandler{

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;


    @Override
    public void doHandle(EventModel model) {
      //
        Message message = new Message();
        User user = userService.getUser(model.getActorId());
        /*message.setFromId(model.getActorId());*/
        message.setFromId(0);
        message.setContent("用户"+user.getName() +
                " 赞了你的资讯,http://127.0.0.1:8080/"+ EntityType.get(model.getEntityType())+"/"
                + String.valueOf(model.getEntityId()));
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
