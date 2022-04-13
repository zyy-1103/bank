package com.bank.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComInfo {
    private String id;
    private String quantity;
    private String price;
    private String total;
    private String startTime;
    private String endTime;
    private String isOver;
    private String remain;
}
