package com.bank.test;

import com.alibaba.fastjson.JSONObject;
import com.bank.bean.ComInfo;
import com.bank.service.SeckillService;
import com.bank.utils.SM3;

import java.io.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        System.out.println(SM3.encrypt(String.valueOf(System.currentTimeMillis())));
    }
}
