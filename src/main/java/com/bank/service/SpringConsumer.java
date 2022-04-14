package com.bank.service;

import io.lettuce.core.api.async.RedisAsyncCommands;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "orderTopic", consumerGroup = "springBootGroup",consumeMode = ConsumeMode.ORDERLY)
public class SpringConsumer implements RocketMQListener<String> {
    @Autowired
    RedisAsyncCommands<String,String> commands;

    @Override
    public void onMessage(String s) {

    }
}
