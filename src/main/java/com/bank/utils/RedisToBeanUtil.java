package com.bank.utils;

import com.bank.bean.ComInfo;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.concurrent.ExecutionException;

public class RedisToBeanUtil {
    public static ComInfo getComInfo(RedisAsyncCommands<String, String> commands) throws ExecutionException, InterruptedException {
        ComInfo comInfo = new ComInfo();
        comInfo.setEndTime(commands.get("_end").get());
        comInfo.setStartTime(commands.get("_start").get());
        comInfo.setPrice(commands.get("_price").get());
        comInfo.setQuantity(commands.get("_quantity").get());
        comInfo.setTotal(commands.get("_total").get());
        return comInfo;
    }
}
