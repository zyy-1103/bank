package com.bank.service;

import com.alibaba.fastjson.JSONObject;
import com.bank.bean.ComInfo;
import com.bank.utils.RedisToBeanUtil;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class SeckillService {
    @Autowired
    RedisAsyncCommands<String, String> commands;

    public String getComInfo() throws ExecutionException, InterruptedException {
        if (commands.get("_start") == null) {
            return null;
        }
        ComInfo comInfo = RedisToBeanUtil.getComInfo(commands);
        JSONObject object = new JSONObject();
        object.put("data", comInfo);
        return object.toJSONString();
    }
}
