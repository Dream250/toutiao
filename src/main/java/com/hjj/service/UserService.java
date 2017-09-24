package com.hjj.service;

import com.hjj.dao.LoginTicketDAO;
import com.hjj.dao.UserDAO;
import com.hjj.model.LoginTicket;
import com.hjj.model.User;
import com.hjj.util.Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2017/7/8.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    //注册
    public Map<String,Object> register(String username,String password,String email){
        Map<String,Object> map=new HashMap<String,Object>();
        //判断是否为空（注意：前后端都需要验证）
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空！");
            return map;
        }
        if(StringUtils.isBlank(email)){
            map.put("msgemail","邮箱不能为空！");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(null!=user){
            map.put("msgname","用户名已被注册！");
            return map;
        }

        user=new User();
        user.setName(username);
        //设置盐
        user.setSalt(UUID.randomUUID().toString().substring(0,5));

        Random rand=new Random();
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", rand.nextInt(100)));

        //密码加密
        user.setPassword(Util.MD5(password+user.getSalt()));

        user.setEmail(email);
        userDAO.addUser(user);

        map.put("userId",user.getId());
        //注册后登录，发放ticket值
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    //登录
    public Map<String,Object> login(String username,String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空！");
            return map;
        }

        User user=userDAO.selectByName(username);
        if(null==user){
            map.put("msgname","用户名不存在！");
            return map;
        }
        if(!user.getPassword().equals(Util.MD5(password+user.getSalt()))){
            map.put("msgpwd","密码错误！");
            return map;
        }

        map.put("userId",user.getId());
        map.put("email",user.getEmail());
        //发放ticket值
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    private String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0); //0表示未过期
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addLoginTicket(ticket);
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public User getUserByName(String name){
        return userDAO.selectByName(name);
    }

    //退出登录
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);//1代表ticket过期
    }

    //修改密码
    public boolean modifyPassword(int id ,String password){
        return userDAO.updatePassword(id,password);
    }

   /* //根据id 判断用户的权限
    public int selectAuthorityById(int id){
        return userDAO.selectAuthorityById(id);
    }*/
}
