package com.bank.controller;

import com.bank.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

@RestController
public class SeckillController {
    @Autowired
    SeckillService service;

    @PostMapping(value = "getComInfo")
    public String getComInfo(@RequestBody String s) throws ExecutionException, InterruptedException {
        return service.getComInfo(s);
    }

    @PostMapping(value = "getTime")
    public String getTime() throws ParseException, ExecutionException, InterruptedException {
        return service.getTime();
    }

    @PostMapping(value = "getUrl")
    public String getUrl(@RequestBody String s) throws ExecutionException, InterruptedException {
        return service.getUrl(s);
    }

    @PostMapping(value = "seckill/{url}")
    public String go(@RequestBody String s,@PathVariable String url) throws ExecutionException, InterruptedException {
        return service.go(s, url);
    }


}
