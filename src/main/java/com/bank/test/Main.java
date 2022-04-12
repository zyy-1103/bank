package com.bank.test;

import java.io.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Callable<Integer> callable=new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(3000);
                return 1;
            }
        };
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 12, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
        ConcurrentLinkedQueue<Callable<Integer>> queue = new ConcurrentLinkedQueue<>();
        queue.add(callable);

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.submit(() -> {
                try {
                    System.out.println(1);
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "你好";
            });
        }

    }
}
