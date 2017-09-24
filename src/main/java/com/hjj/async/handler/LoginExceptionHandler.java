package com.hjj.async.handler;

import com.hjj.async.EventHandler;
import com.hjj.async.EventModel;
import com.hjj.async.EventType;
import com.hjj.model.Message;
import com.hjj.service.MailSender;
import com.hjj.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Administrator on 2017/8/8.
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("成功登录！");
        // SYSTEM ACCOUNT
        message.setFromId(0);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);

     /*   Map<String, Object> map = new HashMap();
        map.put("username", model.getExt("username"));

        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登录成功",
                "mails/welcome.html", map);*/
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}

