package com.bank.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBean {
    private int id;
    private String user;
    private String age;
    private String workState;
    private String email;
    private String password;
    private String address;
    private int sucTimes;
    private int failTimes;
}
