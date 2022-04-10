package com.bank.service;

import org.apache.ibatis.annotations.Select;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SeckillServiceTest {

    @Autowired
    SeckillService service;

    @Test
    void isCapable() throws ExecutionException, InterruptedException {
        int capable = service.isCapable("1");
        System.out.println(capable);
    }
}