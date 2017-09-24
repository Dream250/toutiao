package com.hjj.interceptor;

import com.hjj.dao.LoginTicketDAO;
import com.hjj.dao.UserDAO;
import com.hjj.model.HostHolder;
import com.hjj.model.LoginTicket;
import com.hjj.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/10.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
            throws Exception {
        String ticket=null;
        if(null!=httpServletRequest.getCookies()){
            for(Cookie cookie : httpServletRequest.getCookies()){
                if("ticket".equals(cookie.getName())) {
                    ticket=cookie.getValue();
                    break;
                }
            }
        }
        if(null!=ticket){
            LoginTicket loginTicket=loginTicketDAO.selectTicketByTicket(ticket);
            //验证ticket是否有效
            if(null==loginTicket||loginTicket.getExpired().before(new Date())||
                    loginTicket.getStatus()!=0)
                return true;
            User user=userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o,
                                ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null && hostHolder.getUser()!=null){
            User user=hostHolder.getUser();
            String username=user.getName();
            int authority=user.getAuthority();
            modelAndView.addObject("user",user);
            modelAndView.addObject("localhostUserName",username);
            modelAndView.addObject("authority",authority);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}