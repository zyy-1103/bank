package com.bank.service;

import com.bank.bean.OrderFormBean;
import com.bank.mapper.SeckillMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(consumerGroup = "springBootGroup", topic = "generate-order")
public class GenerateOrderConsumer implements RocketMQListener<OrderFormBean> {

    @Autowired
    SeckillMapper mapper;

    /**
     * 生成订单
     * @param bean 订单内容
     */
    @Override
    public void onMessage(OrderFormBean bean) {
        System.out.println("生成订单");
        mapper.insert(bean);
    }
}
