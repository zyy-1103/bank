package com.bank.service;

import com.bank.bean.ComInfo;
import com.bank.bean.UserBean;
import com.bank.mapper.BackMapper;
import com.bank.mapper.ComInfoMapper;
import com.bank.utils.SM3;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class BackService {
    @Autowired
    RedisAsyncCommands<String, String> commands;
    @Autowired
    BackMapper mapper;
    @Autowired
    ComInfoMapper infoMapper;

    public void init() throws ParseException {
        ComInfo notOver = mapper.getNotOver();
        if (notOver != null) {
            commands.set("_start", notOver.getStartTime());
            commands.set("_end", notOver.getEndTime());
            commands.set("_quantity", notOver.getQuantity());
            commands.set("_total", notOver.getTotal());
            commands.set("_price", notOver.getPrice());
            commands.set("_remain", notOver.getTotal());
        }
    }

    public String config(ComInfo info) {
        System.out.println("here");
        info.setId("0");
        int insert = infoMapper.insert(info);
        return String.valueOf(insert);
    }

    public String login(UserBean bean) {
        bean.setPassword(SM3.encrypt(bean.getPassword()));
        return String.valueOf(mapper.isUser(bean.getUser(), bean.getPassword()));
    }
}
