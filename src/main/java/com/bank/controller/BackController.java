package com.bank.controller;

import com.bank.bean.ComInfo;
import com.bank.bean.OverdueRuleBean;
import com.bank.bean.UserBean;
import com.bank.bean.UserRuleBean;
import com.bank.service.BackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping(value = "/back")
public class BackController {

    @Autowired
    BackService service;

    @PostMapping(value = "config")
    public String config(@RequestBody ComInfo comInfo) throws ParseException {
        return service.config(comInfo);
    }

    @RequestMapping(value = "getOrder")
    public String getOrder(){
        return service.getOrder();
    }

    /**
     *     如果出现意外情况导致服务器宕机，可以发送请求
     *     手动将最近一次商品信息读入redis中
     */
    @GetMapping(value = "startLatest")
    public void start() throws ParseException {
        service.init();
    }

    @PostMapping(value = "overdue")
    public String overdue(@RequestBody OverdueRuleBean bean) {
        return service.overdue(bean);
    }

    @PostMapping(value = "userRule")
    public String userRule(@RequestBody UserRuleBean bean) {
        return null;
    }

    @PostMapping(value = "login")
    public String login(@RequestBody UserBean bean) {
        return service.login(bean);
    }

    @RequestMapping(value = "getTestData")
    public String getData() {
        return service.getDataTest();
    }

    @RequestMapping(value = "getAll")
    public String getAll(){
        return service.getAll();
    }
}
