package com.bank.controller;

import com.bank.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class SeckillController {
    @Autowired
    SeckillService service;

    @PostMapping(value = "getComInfo")
    public String getComInfo() throws ExecutionException, InterruptedException {
        return service.getComInfo();
    }
}
