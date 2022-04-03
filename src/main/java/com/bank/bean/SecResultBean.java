package com.bank.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecResultBean {
    private long id;
    private long peopleNum;
    private long spendTime;
    private Date date;
    private long price;
    private long quantity;
}
