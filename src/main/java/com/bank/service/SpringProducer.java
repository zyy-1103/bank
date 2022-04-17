package com.bank.service;

import com.bank.bean.OrderFormBean;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
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
    Callback callback = new Callback();

    public void generateOrder(OrderFormBean bean) {
        String topic = "generate-order";
//        rocketMQTemplate.asyncSend(topic, bean, callback);
        rocketMQTemplate.syncSend(topic, bean);
    }

    public void listener(OrderFormBean bean) {
        String topic = "delay-listener";
//        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(bean).build(), callback, 5000, 12);
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(bean).build(), 5000, 2);
    }
}
