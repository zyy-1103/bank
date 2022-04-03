package com.bank.controller;

import com.bank.bean.UserBean;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired
    UserService service;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestBody UserBean bean,HttpServletResponse r){
        return service.login(bean, r);
    }
}
