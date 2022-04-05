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

    @GetMapping(value = "start")
    public void test() throws ParseException {
        service.init();
    }

    @PostMapping(value = "config")
    public String config(@RequestBody ComInfo comInfo) {
        return service.config(comInfo);
    }

    @PostMapping(value = "overdue")
    public String overdue(@RequestBody OverdueRuleBean bean) {
        return null;
    }

    @PostMapping(value = "userRule")
    public String userRule(@RequestBody UserRuleBean bean) {
        return null;
    }

    @PostMapping(value = "login")
    public String login(UserBean bean) {
        return service.login(bean);
    }

    @GetMapping("/test")
    public String test1() {

        return "success";
    }
}
