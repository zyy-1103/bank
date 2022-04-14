package com.bank.service;

import com.bank.bean.OrderFormBean;
import com.bank.mapper.SeckillMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "delay-listener", consumerGroup = "springBootGroup")
public class DelayListenerConsumer implements RocketMQListener<OrderFormBean> {

    @Autowired
    SeckillMapper mapper;

    /**
     * 8分钟延时消息
     * 若用户8分钟内未完成支付，则删除订单
     * @param bean 订单内容
     */
    @Override
    public void onMessage(OrderFormBean bean) {
        System.out.println("删除订单");
        if (mapper.isFinished(bean.getUserId(), bean.getSeckillId())!=0) {
            mapper.delUnFinished(bean.getUserId(), bean.getSeckillId());
        }
    }
}
