package com.bank.test;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        long s=System.currentTimeMillis()+5000;
        ScheduledFuture<?> scheduledFuture = Executors.newScheduledThreadPool(1)
                .scheduleWithFixedDelay(() -> {
                    System.out.println(System.currentTimeMillis());
                }, 1, 1, TimeUnit.SECONDS);
        if (System.currentTimeMillis() > s) {
            scheduledFuture.cancel(false);
        }
    }
}
