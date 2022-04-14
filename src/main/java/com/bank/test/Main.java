package com.bank.test;

import com.alibaba.fastjson.JSONObject;
import com.bank.bean.ComInfo;
import com.bank.service.SeckillService;
import com.bank.utils.SM3;
import org.yaml.snakeyaml.scanner.ScannerImpl;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(format.format(new Date().getTime()));
    }
}
