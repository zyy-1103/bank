package com.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SpringProducerTest {

    @Autowired
    SpringProducer producer;
    @Test
    void sendMsg() throws InterruptedException {
        producer.sendMsg("orderTopic", "hello","1");
        System.out.println("完成");
    }
}