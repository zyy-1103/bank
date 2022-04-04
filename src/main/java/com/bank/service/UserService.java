package com.bank.service;

import com.bank.bean.UserBean;
import com.bank.mapper.UserMapper;
import com.bank.utils.SM3;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {
    public static final long USER_TIME_OUT=60*60*2;

    @Autowired
    UserMapper mapper;
    @Autowired
    RedisAsyncCommands<String, String> commands;
    JSONObject object = new JSONObject();

    public String login(UserBean bean, HttpServletResponse response) {
        bean.setPassword(SM3.encryptWithSalt(bean.getPassword()));
        Integer id = mapper.isUser(bean);
        object.clear();
//        登录成功
        if (id != null) {
            object.put("data", "1");
//            用户登录状态保存在redis中
//            value默认为0，若后面有资格参加秒杀，则将value设为1
            commands.setex("u" + id, USER_TIME_OUT, "0");
//            客户端用cookie保存token
            Cookie idCookie = new Cookie("id", id.toString());
            Cookie wordCookie = new Cookie("word", SM3.encryptWithSalt(id.toString()));
            idCookie.setPath("/");
            wordCookie.setPath("/");
            idCookie.setMaxAge((int)UserService.USER_TIME_OUT);
            wordCookie.setMaxAge((int)UserService.USER_TIME_OUT);
            response.addCookie(idCookie);
            response.addCookie(wordCookie);
        } else {
            object.put("data", "用户名或密码错误");
        }
        return object.toJSONString();
    }
}
