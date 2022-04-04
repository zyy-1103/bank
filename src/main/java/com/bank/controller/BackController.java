package com.bank.controller;

import com.bank.service.RuleReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/back")
public class BackController {

    @Autowired
    RuleReadService service;

    @GetMapping(value = "test")
    public String test(){
        return "hello";
    }
}
