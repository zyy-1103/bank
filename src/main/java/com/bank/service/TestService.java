package com.bank.service;

import com.bank.bean.SecResultPersonBean;
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

@Service
public class TestService {
    @Autowired
    UserMapper mapper;

    //模拟用户数据
    public void insUser() throws IOException {
        Random random = new Random();
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Administrator\\Desktop\\data.txt"));
        Province[] values = Province.values();
        WorkState[] states = WorkState.values();
        int id = 1227;
        UserBean userBean = new UserBean(id, "1", "1", "1", "abc@ABC", SM3.encrypt("123456"), "1", 1, 1);
        String s = reader.readLine();
        for (String i : s.split("\t")) {
            userBean.setUser(i);
            userBean.setId(id++);
            userBean.setAge(String.valueOf(Math.abs(random.nextInt()) % 65 + 15));
            int i1 = Math.abs(random.nextInt()) % 17 + Math.abs(random.nextInt()) % 18;
            userBean.setAddress(values[i1].name());
            userBean.setWorkState(states[Math.abs(random.nextInt()) % 7 + Math.abs(random.nextInt()) % 7].name());
            userBean.setSucTimes(Math.abs(random.nextInt())%20);
            userBean.setFailTimes(Math.abs(random.nextInt())%20);
            mapper.insert(userBean);
        }
    }

    public void insRP(){
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            int uid = Math.abs(random.nextInt()) % 10000 + 609;
            int sid = Math.abs(random.nextInt()) % 31;
            mapper.insertRP(uid, sid);
        }

    }
}
