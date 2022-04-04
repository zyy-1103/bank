package com.bank.controller;

import com.bank.service.RuleReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping(value = "/back")
public class BackController {

    @Autowired
    RuleReadService service;

    @GetMapping(value = "start")
    public void test() throws ParseException {
        service.init();
    }
}
