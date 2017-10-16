package com.hjj.controller;


import com.hjj.model.HostHolder;
import com.hjj.model.Message;
import com.hjj.model.User;
import com.hjj.model.ViewObject;
import com.hjj.service.MessageService;
import com.hjj.service.UserService;
import com.hjj.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model){
        try{
            int localUserId=hostHolder.getUser().getId();
            List<Message> conversationSystemLetterList = messageService.getConversationSystemLetterList(localUserId,0,100);
            int totalSystemLetter = conversationSystemLetterList.size();
            ViewObject systemVo = new ViewObject();
            systemVo.set("user",userService.getUser(0));
            systemVo.set("unread",messageService.getSystemLetterUnreadCount(localUserId));
            systemVo.set("total",totalSystemLetter);
            systemVo.set("msg",conversationSystemLetterList.get(0));
            model.addAttribute("systemVo",systemVo);

            List<Message> converstaionFriendLetterList = messageService.getConversionFriendLetterList(localUserId,0,50);
            List<ViewObject> friendLetterList = new ArrayList<ViewObject>();
            for(Message msg : converstaionFriendLetterList){
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("unread", messageService.getFriendLetterUnreadCount(localUserId, msg.getConversationId()));
                friendLetterList.add(vo);
            }
            model.addAttribute("friendLetter",friendLetterList);
        }catch (Exception e){
            logger.error("获取站内信列表失败！"+e.getMessage());
        }
        return "letter";
    }

     @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
     public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<ViewObject> messages = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 50);
            for (Message msg : conversationList) {
                if(msg.getToId()==hostHolder.getUser().getId())
                    messageService.updateHasRead(msg.getId(),1); // 已读
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                String from_to="系统通知";
                if(msg.getFromId()!=0) {
                    String fromName = userService.getUser(msg.getFromId()).getName();
                    String toName = userService.getUser(msg.getToId()).getName();
                    from_to=fromName+"--->"+toName;
                }
                vo.set("fromId",msg.getFromId());
                vo.set("fromto",from_to);
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            model.addAttribute("conversationId",conversationId);
            return "letterDetail";
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path={"/msg/addMessage"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String addMessage(
            @RequestParam("toName") String toName,
            @RequestParam("content") String content
    ){
        try {
            Message msg = new Message();
            int toId = userService.getUserByName(toName).getId();
            int fromId = hostHolder.getUser().getId();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            msg.setConversationId(fromId < toId ? String.format("%d_%d", toId, fromId) : String.format("%d_%d", fromId, toId));
            messageService.addMessage(msg);
            return Util.getJSONString(1, "成功");
        }catch (Exception e){
            logger.info("失败！"+e.getMessage());
            return Util.getJSONString(0,"失败");
        }
    }

    /*@RequestMapping(path={"/msg/addMessage"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(
            @RequestParam("fromId") int fromId,
            @RequestParam("toId") int toId,
            @RequestParam("content") String content
    ){
        try {
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            msg.setConversationId(fromId < toId ? String.format("%d_%d", toId, fromId) : String.format("%d_%d", fromId, toId));
            messageService.addMessage(msg);
            return Util.getJSONString(1, "成功");
        }catch (Exception e){
            logger.info("失败！"+e.getMessage());
            return Util.getJSONString(0,"失败");
        }
    }*/

    @RequestMapping("/msg/delete")
     public String deleteMessage(@RequestParam("messageId") int messageId,
                                 @RequestParam("conversationId") String conversationId){
        messageService.deleteMessage(messageId,1);
        return "redirect:/msg/detail?conversationId="+conversationId;
    }

}
