package com.bank.controller;

import com.bank.bean.UserBean;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired
    UserService service;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody UserBean bean, HttpServletResponse r) {
        return service.login(bean, r);
    }

    @PostMapping(value = "/register")
    public String register(@RequestBody UserBean bean) {
        return service.register(bean);
    }

}
