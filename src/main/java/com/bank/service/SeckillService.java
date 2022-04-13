package com.bank.service;

import com.alibaba.fastjson.JSONObject;
import com.bank.bean.ComInfo;
import com.bank.mapper.BackMapper;
import com.bank.mapper.ComInfoMapper;
import com.bank.utils.RedisToBeanUtil;
import com.bank.utils.SM3;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.*;

@Service
public class SeckillService {
    @Autowired
    RedisAsyncCommands<String, String> commands;
    @Autowired
    ComInfoMapper mapper;
    @Autowired
    BackService service;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public String getComInfo(String s) throws ExecutionException, InterruptedException {
        JSONObject jsonObject = JSONObject.parseObject(s);
        String id = jsonObject.getString("id");
        String word = jsonObject.getString("word");
        String cid = commands.get("u" + id).get();

        //登录信息无效或错误
        if (cid == null || id == null || word == null || !word.equals(SM3.encryptWithSalt(id))) {
            return null;
        }

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
        return String.valueOf(dateFormat.parse(start).getTime());
    }

    //判断该用户是否有资格参与秒杀
    public int  isCapable(String id) throws ExecutionException, InterruptedException {
        //用户已标记为1，可以参加秒杀
        if (commands.get("u" + id).get().equals("1")) {
            return 1;
        }
        String sql = commands.get("~odsql").get();
        String cnt = commands.get("~odcnt").get();
        String ope = commands.get("~odope").get();
        if (sql == null || cnt == null || ope == null) {
            commands.set("u" + id, "1");
            return 1;
        }
        int realCnt = mapper.capable(sql, id);
        switch (ope) {
            case ">":
                if (realCnt <= Integer.parseInt(cnt)) {
                    commands.set("u"+id,"1");
                    return 1;
                }
                return 0;
            case "<":
                if(realCnt>=Integer.parseInt(cnt)){
                    commands.set("u" + id, "1");
                    return 1;
                }
                return 0;
            default:
                return 0;
        }
    }

    public String getUrl(String s) throws ExecutionException, InterruptedException {
        JSONObject object = JSONObject.parseObject(s);
        String id = object.getString("id");
        String word = object.getString("word");
        if (id == null || word == null || !word.equals(SM3.encryptWithSalt(id))) {
            return null;
        }
        if ("1".equals(commands.get("u" + id).get())) {
            return commands.get("_url").get();
        }
        return null;
    }

    public String go(String s, String url) throws ExecutionException, InterruptedException {
        JSONObject object = JSONObject.parseObject(s);
        String id = object.getString("id");
        if (url.equals(commands.get("_url").get())) {
            Long aLong = commands.decr("_remain").get();
            if (aLong < 0) {
                return "已售罄";
            }else {
                commands.incr("_sum");
                service.update();
                return "秒杀成功";
            }
        }
        return "已售罄";
    }
}
