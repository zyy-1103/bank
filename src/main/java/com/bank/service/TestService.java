package com.bank.service;

import com.bank.bean.UserBean;
import com.bank.mapper.UserMapper;
import com.bank.utils.Province;
import com.bank.utils.SM3;
import com.bank.utils.WorkState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

@Service
public class TestService {
    @Autowired
    UserMapper mapper;

    class R implements Runnable{
        UserBean bean;

        public void setBean(UserBean bean) {
            this.bean = bean;
        }

        @Override
        public void run() {
            mapper.insert(bean);
        }
    }

    //模拟用户数据
    public void insUser() throws IOException, InterruptedException, ExecutionException {
        Random random = new Random();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 12, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));
        ExecutorService executorService = Executors.newFixedThreadPool(12);
        ExecutorService executorService1 = Executors.newCachedThreadPool();
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Administrator\\Desktop\\data2.txt"));
        Province[] values = Province.values();
        WorkState[] states = WorkState.values();
        int id=35651;
        String s;
        while ((s = reader.readLine()) != null) {
            for (String i : s.split("\t")) {
                UserBean userBean = new UserBean(id, "1", "1", "1", "@ABC", SM3.encrypt("123456"), "1", "1", 1, 1);
                userBean.setUser(i);
                userBean.setEmail(id+"@ABC");
                userBean.setId(id++);
                userBean.setAge(String.valueOf(Math.abs(random.nextInt()) % 65 + 15));
                int i1 = Math.abs(random.nextInt()) % 17 + Math.abs(random.nextInt()) % 18;
                userBean.setAddress(values[i1].name());
                userBean.setWorkState(states[Math.abs(random.nextInt()) % 7 + Math.abs(random.nextInt()) % 7].name());
                userBean.setSucTimes(Math.abs(random.nextInt())%20);
                userBean.setFailTimes(Math.abs(random.nextInt())%20);
                executorService.submit(()->{
                    mapper.insert(userBean);
                });
            }
        }
        Thread.sleep(1000*60*120);
    }

    public void insRP(){
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            int uid = Math.abs(random.nextInt()) % 10000 + 609;
            int sid = Math.abs(random.nextInt()) % 31;
            mapper.insertRP(uid, sid);
        }

    }

    public void insOd(){

    }
}
