package com.bank.service;

import com.bank.bean.ComInfo;
import com.bank.mapper.BackMapper;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class RuleReadService{
    @Autowired
    RedisAsyncCommands<String, String> commands;
    @Autowired
    BackMapper mapper;

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
}
