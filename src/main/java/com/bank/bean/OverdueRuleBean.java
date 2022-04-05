package com.bank.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverdueRuleBean {
    private String start;
    private String end;
    private String count;
    private String operator;
    private String exCount;
    private String exCountOperator;
    private String exTime;
    private String exTimeUnit;
    private String exTimeOperator;
}
