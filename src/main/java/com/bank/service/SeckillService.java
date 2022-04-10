package com.bank.service;

import com.alibaba.fastjson.JSONObject;
import com.bank.bean.ComInfo;
import com.bank.mapper.ComInfoMapper;
import com.bank.utils.RedisToBeanUtil;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

@Service
public class SeckillService {
    @Autowired
    RedisAsyncCommands<String, String> commands;
    @Autowired
    ComInfoMapper mapper;

    public String getComInfo(String s) throws ExecutionException, InterruptedException {
        JSONObject jsonObject = JSONObject.parseObject(s);
        String id = jsonObject.getString("id");
        String word = jsonObject.getString("word");
        String cid = commands.get("u" + id).get();


        if (commands.get("_start").get() == null) {
            return null;
        }
        ComInfo comInfo = RedisToBeanUtil.getComInfo(commands);
        JSONObject object = new JSONObject();
        object.put("data", comInfo);
        object.put("capable", isCapable(id));
        return object.toJSONString();
    }

    public String getTime() throws ExecutionException, InterruptedException, ParseException {
        String start = commands.get("_start").get();
        if (start== null) {
            return String.valueOf(-1);
        }
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(start).getTime());
        return String.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(start).getTime());
    }

    //判断该用户是否有资格参与秒杀
    public int  isCapable(String id) throws ExecutionException, InterruptedException {
        String sql = commands.get("~odsql").get();
        String cnt = commands.get("~odcnt").get();
        String ope = commands.get("~odope").get();
        if (sql == null || cnt == null || ope == null) {
            return 0;
        }
        int realCnt = mapper.capable(sql, id);
        switch (ope) {
            case ">":
                return realCnt > Integer.parseInt(cnt) ? 0 : 1;
            case "<":
                return realCnt < Integer.parseInt(cnt) ? 0 : 1;
            default:
                return 0;
        }
    }
}
