package com.bank.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user")
public class UserBean {
    private int id;
    private String user;
    private String age;
    private String workState;
    private String email;
    private String password;
    private String address;
    private String idNum;
    private int sucTimes;
    private int failTimes;
}
