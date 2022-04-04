package com.bank.service;

import com.bank.bean.ComInfo;
import com.bank.mapper.BackMapper;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class RuleReadService implements Runnable{
    @Autowired
    RedisAsyncCommands<String, String> commands;
    @Autowired
    BackMapper mapper;
    ScheduledFuture<?> future;

    public void init(){
        ScheduledExecutorService service= Executors.newScheduledThreadPool(1);
        future = service.scheduleAtFixedRate(new RuleReadService(), 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
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
