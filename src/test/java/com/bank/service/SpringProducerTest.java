package com.bank.service;

import com.bank.bean.OrderFormBean;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SpringProducerTest {

    @Autowired
    SpringProducer producer;
    @Test
    void sendMsg() throws InterruptedException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String s = format.format(new Date());
        OrderFormBean bean = new OrderFormBean(0, 1, 1, 0, s);
        producer.generateOrder(bean);
        producer.listener(bean);
        Thread.sleep(100000);

    }
}