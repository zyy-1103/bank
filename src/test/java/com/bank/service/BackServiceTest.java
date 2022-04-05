package com.bank.service;

import com.bank.bean.ComInfo;
import com.bank.bean.OverdueRuleBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class BackServiceTest {

    @Autowired
    BackService service;

    @Test
    void config() {
        ComInfo comInfo = new ComInfo("0", "1000", "1000", "1000", "2020-01-01 00:00:00", "2020-01-03 00:00:00", "0");
        String config = service.config(comInfo);
        System.out.println(config);
        Assertions.assertEquals(config, "1");
    }

    @Test
    void login(){

    }

    @Test
    void overdue(){
        OverdueRuleBean bean = new OverdueRuleBean("2000-01-01", "2022-4-5", "0", ">", "0", ">=", "3", "day", ">=");
        service.overdue(bean);
    }
}