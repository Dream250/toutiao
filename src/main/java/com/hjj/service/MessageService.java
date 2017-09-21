package com.hjj.service;

import com.hjj.dao.MessageDAO;
import com.hjj.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    //系统信息 0为系统用户
    public List<Message> getConversationSystemLetterList(int userId, int offset, int limit){
        return messageDAO.getConversationSystemList(userId, offset, limit,0);
    }

    //朋友私信
    public List<Message> getConversionFriendLetterList(int userId, int offset, int limit){
        return messageDAO.getConversationFriendList(userId,offset,limit,0);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public int getUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnReadCount(userId, conversationId);
    }

    //系统信息 未读数量
    public int getSystemLetterUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationSystemLetterUnReadCount(userId, conversationId, 0);
    }
    //朋友私信 未读数量
    public int getFriendLetterUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationFriendLetterUnReadCount(userId, conversationId,0);
    }

    public void deleteMessage(int id,int status){
        messageDAO.updateStatus(id,status);
    }

    public void updateHasRead(int id,int hasRead){
        messageDAO.updateHasRead(id,hasRead);
    }

}
