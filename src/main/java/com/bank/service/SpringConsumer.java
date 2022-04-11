package com.bank.service;

import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
public class SpringConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {

    }
}
