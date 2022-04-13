package com.bank.service;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class SpringProducer {
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    static class Callback implements SendCallback{
        @Override
        public void onSuccess(SendResult sendResult) {
            System.out.println(sendResult.getMsgId()+":"+sendResult.getSendStatus().name());
        }

        @Override
        public void onException(Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void sendMsg(String topic,String msg,String key) throws InterruptedException {
        String hashKey = String.valueOf(String.valueOf(System.currentTimeMillis()).hashCode());
        Callback callback = new Callback();
        rocketMQTemplate.asyncSendOrderly(topic, msg + "-1", key, callback);
        rocketMQTemplate.asyncSendOrderly(topic, msg + "-2", key, callback);
        rocketMQTemplate.asyncSendOrderly(topic, msg + "-3", key, callback);
        Thread.sleep(5000);
//        rocketMQTemplate.syncSendOrderly(topic, msg + "-1", hashKey);
//        rocketMQTemplate.syncSendOrderly(topic, msg + "-2", hashKey);
//        rocketMQTemplate.syncSendOrderly(topic, msg + "-3", hashKey);
    }
}
