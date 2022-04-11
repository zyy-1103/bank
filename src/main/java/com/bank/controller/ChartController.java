package com.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "back")
public class ChartController {

    @GetMapping(value = "getEarth/{id}")
    public String toEarth(){
        return "../earth";
    }

    @PostMapping(value = "getEarth/{id}")
    public String getEarth(@PathVariable String id) {
        return "1";
    }
}
