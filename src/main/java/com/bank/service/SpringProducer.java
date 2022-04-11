package com.bank.service;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringProducer {
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    public void sendMsg(String topic,String msg) {
        rocketMQTemplate.convertAndSend(topic, msg);
    }
}
