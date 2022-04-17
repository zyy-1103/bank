package com.bank.service;

import com.bank.bean.UserBean;
import com.bank.mapper.UserMapper;
import com.bank.utils.SM3;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Service
public class UserService {
    public static final long USER_TIME_OUT=60*60*2;

    @Autowired
    UserMapper mapper;
    @Autowired
    RedisAsyncCommands<String, String> commands;
    JSONObject object = new JSONObject();

    public String register(UserBean bean) {
        if (StringUtil.isNullOrEmpty(bean.getUser()) || StringUtil.isNullOrEmpty(bean.getPassword()) || StringUtil.isNullOrEmpty(bean.getAddress())
                || StringUtil.isNullOrEmpty(bean.getEmail()) || StringUtil.isNullOrEmpty(bean.getAge()) || StringUtil.isNullOrEmpty(bean.getIdNum())) {
            return "数据不能为空";
        }
        if (!Pattern.matches("[\\u4e00-\\u9fa5]{2,8}", bean.getUser())) {
            return "请输入正确的用户名";
        }
        if (!Pattern.matches("^[A-Za-z0-9]+([_.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}$", bean.getEmail())) {
            return "请输入正确的邮箱";
        }
        if (!Pattern.matches("[0-9Xx]{18}", bean.getIdNum())) {
            return "请输入正确的身份证号码";
        }
        if (!Pattern.matches("[a-zA-Z._0-9]{6,16}", bean.getPassword())) {
            return "请输入正确的密码";
        }
        int registered = mapper.isRegistered(bean.getEmail(), bean.getIdNum());
        if (registered != 0) {
            return "您已注册过用户";
        }
        bean.setSucTimes(0);
        bean.setFailTimes(0);
        return String.valueOf(mapper.insert(bean));
    }

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
            System.out.println(bean.getEmail()+":"+bean.getPassword());
            object.put("data", "用户名或密码错误");
        }
        return object.toJSONString();
    }
}
