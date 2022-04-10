package com.bank.controller;

import com.bank.service.UserService;
import com.bank.utils.CookieUtil;
import com.bank.utils.SM3;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    RedisAsyncCommands<String, String> commands;

    public UserInterceptor(RedisAsyncCommands<String, String> commands) {
        this.commands=commands;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String id = CookieUtil.getCookieValueByName("id", request);
        String word = CookieUtil.getCookieValueByName("word", request);
        //心跳请求，延长redis、cookie中用户标识时间
        if (SM3.encryptWithSalt(id).equals(word)&&commands.get("u"+id)!=null) {
            commands.expire("u" + id, UserService.USER_TIME_OUT);
            Cookie id1 = CookieUtil.getCookieByName("id", request.getCookies());
            Cookie word1 = CookieUtil.getCookieByName("word", request.getCookies());
            if (id1 != null && word1 != null) {
                id1.setMaxAge((int) UserService.USER_TIME_OUT);
                word1.setMaxAge((int) UserService.USER_TIME_OUT);
                response.addCookie(id1);
                response.addCookie(word1);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
