package com.bank.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecResultBean {
    private Long id;
    private Long peopleNum;
    private Long spendTime;
    private Date date;
    private Double price;
    private Long quantity;
    private Double total;
}
