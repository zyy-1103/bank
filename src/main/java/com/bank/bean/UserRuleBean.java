package com.bank.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRuleBean {
    private String workState;
    private String dishonestyLevel;
    private String exclude;
    private String top;
    private String bottom;
}
