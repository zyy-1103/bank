package com.bank.service;

import com.bank.bean.OverdueRuleBean;
import com.bank.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class TestServiceTest {

    @Autowired
    TestService service;
    @Autowired
    UserMapper mapper;

    @Test
    void insUser() throws IOException, InterruptedException, ExecutionException {
        service.insUser();
    }

    @Test
    void insOd(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Random random = new Random();
        for (int i = 2000; i < 3000; i++) {
            long t = System.currentTimeMillis() - Math.abs(random.nextInt() * 200L);
            String date = format.format(new Date(t));
            String amount = String.valueOf(Math.abs(random.nextDouble() * 500000));
            String userId = String.valueOf(Math.abs(random.nextInt()) % 147824);
            String repaymentDate = format.format(new Date(t + Math.abs(random.nextInt() * 10L)));
            mapper.insertOd(i,userId,date,repaymentDate,amount);
        }
    }
}