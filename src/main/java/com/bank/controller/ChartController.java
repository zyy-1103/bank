package com.bank.controller;

import com.bank.service.BackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "back")
public class ChartController {
    @Autowired
    BackService service;

    @GetMapping(value = "getEarth/{id}")
    public String toEarth(){
        return "../earth";
    }

    @PostMapping(value = "getEarth/{id}")
    @ResponseBody
    public String getEarth(@PathVariable String id) {
        return service.getProvinces(id);
    }

    @GetMapping(value = "getAge/{id}")
    public String toAges(){
        return "../age";
    }

    @PostMapping(value = "getAge/{id}")
    @ResponseBody
    public String getAges(@PathVariable String id){
        return service.getAges(id);
    }

    @GetMapping(value = "getWork/{id}")
    public String toWord() {
        return "../work";
    }

    @PostMapping(value = "getWork/{id}")
    @ResponseBody
    public String getWord(@PathVariable String id) {
        return service.getWork(id);
    }
}
