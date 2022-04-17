package com.bank.test;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, SQLException {
        Random random = new Random();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long t = System.currentTimeMillis() - Math.abs(random.nextInt() * 200L);
        System.out.println(format.format(new Date(t)));
        System.out.println(format.format(new Date(t+Math.abs(random.nextInt()*10L))));
        System.out.println(Math.abs(random.nextInt())%147824);
    }
}
